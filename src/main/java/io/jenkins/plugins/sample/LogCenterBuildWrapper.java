package io.jenkins.plugins.sample;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.OutputStream;

public class LogCenterBuildWrapper extends BuildWrapper {

    private LogCenterWriter writer;

    /**
     * Create a new {@link LogCenterBuildWrapper}.
     */
    @DataBoundConstructor
    public LogCenterBuildWrapper() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream decorateLogger(AbstractBuild build, OutputStream logger) {

        OutputStream decorated = logger;

        try {
            writer = new LogCenterAsyncWriter(new LogCenterFileWriter(null));
            decorated = new LogCenterLogDecorator(logger, writer);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        } // Programmer error.

        // Should be the wrapped output stream if everything goes ok
        return decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener)
            throws IOException, InterruptedException {

        writer.setWorkspace(build.getWorkspace());

        return new Environment() {

        };
    }

    @Extension
    public static final class DescriptorImpl extends BuildWrapperDescriptor {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }

        @NonNull
        @Override
        public String getDisplayName() {
            return "Write build logs to working space.";
        }
    }
}
