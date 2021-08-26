package io.jenkins.plugins.sample;

import hudson.FilePath;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Writes lines to local file
 */
public class LogCenterFileWriter implements LogCenterWriter {

    /**
     * UTF-8 output character set.
     */
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private OutputStream outputStream;
    private final StringBuilder buffer;

    /**
     * Constructor
     *
     * @param workspace The workspace for the logfile
     * @throws IOException If there was a problem connecting to logentries.
     */
    public LogCenterFileWriter(FilePath workspace) throws IOException {
        buffer = new StringBuilder(10);
        setWorkspace(workspace);
    }

    public void setWorkspace(FilePath workspace) {
        if (workspace == null) {
            return;
        }

        FilePath output = workspace.child("build.log");
        try {
            this.outputStream = output.write();
            outputStream.write(buffer.toString().getBytes(UTF8));
            buffer.delete(0, buffer.length());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write the given line to LogCenter.
     *
     * @param line The line to write.
     * @throws IOException If there was a problem writing the line.
     */
    public void writeLine(final String line) throws IOException {
        if (outputStream == null) {
            buffer.append(line);
            buffer.append("\n");
            return;
        }
        outputStream.write(line.getBytes(UTF8));
        outputStream.write("\n".getBytes(UTF8));
        outputStream.flush();
    }

    /**
     * Closes this writer.
     */
    public void close() {
        closeStream();
    }

    private void closeStream() {
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
