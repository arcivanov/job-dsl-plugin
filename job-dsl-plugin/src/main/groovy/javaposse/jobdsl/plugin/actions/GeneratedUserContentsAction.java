package javaposse.jobdsl.plugin.actions;

import hudson.model.AbstractProject;
import javaposse.jobdsl.dsl.GeneratedUserContent;

public class GeneratedUserContentsAction
        extends GeneratedObjectsAction<GeneratedUserContent, GeneratedUserContentsBuildAction> {
    public GeneratedUserContentsAction(AbstractProject<?, ?> project) {
        super(project, GeneratedUserContentsBuildAction.class);
    }
}
