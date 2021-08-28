package jenkins.plugins.logcenter;

import hudson.model.Action;
import hudson.model.Item;
import hudson.model.Project;
import org.kohsuke.stapler.StaplerProxy;

@SuppressWarnings("rawtypes")
public class SampleAction implements Action, StaplerProxy {

    private final Project project;

    public SampleAction(Project project) {
        this.project = project;
    }

    public int getBuildStepsCount() {
        return project.getBuilders().size();
    }

    public int getPostBuildStepsCount() {
        return project.getPublishersList().size();
    }

    @Override
    public String getIconFileName() {
        return this.project.hasPermission(Item.CONFIGURE) ? "clipboard.png" : null;
    }

    @Override
    public String getDisplayName() {
        return "Project Statistics";
    }

    @Override
    public String getUrlName() {
        return "stats";
    }

    @Override
    public Object getTarget() {
        this.project.checkPermission(Item.CONFIGURE);
        return this;
    }
}
