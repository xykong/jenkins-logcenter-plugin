package io.jenkins.plugins.sample;

import hudson.FilePath;
import hudson.console.LineTransformationOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public class LogCenterLogDecorator extends LineTransformationOutputStream {

    private final OutputStream wrappedOutputStream;
    private final LogCenterWriter writer;

    /**
     * Constructor
     *
     * @param os The OutputStream to decorate
     */
    public LogCenterLogDecorator(OutputStream os, LogCenterWriter writer) throws IOException {

        this.wrappedOutputStream = os;
        this.writer = writer;
    }

    /**
     * Called when the end of a line is reached.
     */
    @Override
    protected void eol(byte[] bytes, int length) {
        try {
            processLine(bytes, length);
        } catch (IOException | RuntimeException e) {
            // Just print out a trace
            e.printStackTrace();
        } // Don't break the build. Just print out a stack trace.
    }

    // Should we close this here?
    @Override
    public void close() throws IOException {
        writer.close();
        super.close();
        wrappedOutputStream.close();
    }

    private void processLine(byte[] bytes, int length) throws IOException {
        if (length <= 0) {
            return;
        }

        // Find the end before the new line
        int end = length - 1;
        while (bytes[end] == '\n' || bytes[end] == '\r') {
            end--;
        }

        // TODO Verify that the byte are encoded using the platform default (not UTF8)
        if (end > 0) {
            writer.writeLine(new String(bytes, 0, end + 1));
            wrappedOutputStream.write(bytes, 0, length);
        }
    }

    public void setWorkspace(FilePath workspace) {
        this.writer.setWorkspace(workspace);
    }
}
