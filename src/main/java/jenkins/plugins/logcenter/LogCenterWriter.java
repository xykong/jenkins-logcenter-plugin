package jenkins.plugins.logcenter;

import hudson.FilePath;

import java.io.IOException;

/**
 * Writes lines to LogCenter.
 */
public interface LogCenterWriter {

    /**
     * Writes the given line to LogCenter.
     *
     * @param line The line to write.
     * @throws IOException If there was a problem writing the line.
     */
    void writeLine(final String line) throws IOException;

    /**
     * Closes this writer.
     */
    void close();

    void setWorkspace(FilePath workspace);
}
