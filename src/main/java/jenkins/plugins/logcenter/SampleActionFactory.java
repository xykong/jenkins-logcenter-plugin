package jenkins.plugins.logcenter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Action;
import hudson.model.Project;
import jenkins.model.TransientActionFactory;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("rawtypes")
@Extension
public class SampleActionFactory extends TransientActionFactory<Project> {

    @Override
    public Class<Project> type() {
        return Project.class;
    }

    @NonNull
    @Override
    public Collection<? extends Action> createFor(@NonNull Project project) {
        return Collections.singleton(new SampleAction(project));
    }
}
