package jenkins.plugins.logcenter;

import hudson.FilePath;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class to write logs to logentries asynchronously
 */
public class LogCenterAsyncWriter implements LogCenterWriter {

    private static final int SHUTDOWN_TIMEOUT_SECONDS = 10;

    private final ExecutorService executor;
    private final LogCenterWriter writer;

    /**
     * Constructor.
     *
     * @param writer Used to write entries to LogCenter.
     */
    public LogCenterAsyncWriter(LogCenterWriter writer) {
        this.executor = Executors.newSingleThreadExecutor();
        this.writer = writer;
    }

    /**
     * Writes the given string to logentries.com asynchronously. It would be
     * possible to take an array of bytes as a parameter, but we want to make
     * sure it is UTF8 encoded.
     *
     * @param line The line to write.
     */
    public void writeLine(final String line) {
        executor.execute(() -> {
            try {
                writer.writeLine(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        try {
            executor.shutdown();
            if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                System.err.println("LogCenterWriter shutdown before finished execution");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
            writer.close();
        }
    }

    @Override
    public void setWorkspace(FilePath workspace) {
        this.writer.setWorkspace(workspace);
    }
}
