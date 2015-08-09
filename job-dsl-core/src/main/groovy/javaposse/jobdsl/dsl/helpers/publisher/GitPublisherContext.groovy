package javaposse.jobdsl.dsl.helpers.publisher

import javaposse.jobdsl.dsl.AbstractContext
import javaposse.jobdsl.dsl.ContextHelper
import javaposse.jobdsl.dsl.DslContext
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.RequiresPlugin

import static javaposse.jobdsl.dsl.Preconditions.checkNotNullOrEmpty

class GitPublisherContext extends AbstractContext {
    boolean pushOnlyIfSuccess
    boolean pushMerge
    boolean forcePush
    List<Node> tags = []
    List<Node> branches = []

    GitPublisherContext(JobManagement jobManagement) {
        super(jobManagement)
    }

    void pushOnlyIfSuccess(boolean pushOnlyIfSuccess = true) {
        this.pushOnlyIfSuccess = pushOnlyIfSuccess
    }

    void pushMerge(boolean pushMerge = true) {
        this.pushMerge = pushMerge
    }

    /**
     * @since 1.27
     */
    @RequiresPlugin(id = 'git', minimumVersion = '2.2.6')
    void forcePush(boolean forcePush = true) {
        this.forcePush = forcePush
    }

    void tag(String targetRepo, String name, @DslContext(TagToPushContext) Closure closure = null) {
        checkNotNullOrEmpty(targetRepo, 'targetRepo must be specified')
        checkNotNullOrEmpty(name, 'name must be specified')

        TagToPushContext context = new TagToPushContext()
        ContextHelper.executeInContext(closure, context)

        tags << NodeBuilder.newInstance().'hudson.plugins.git.GitPublisher_-TagToPush' {
            targetRepoName(targetRepo)
            tagName(name)
            tagMessage(context.message ?: '')
            createTag(context.create)
            updateTag(context.update)
        }
    }

    void branch(String targetRepo, String name) {
        checkNotNullOrEmpty(targetRepo, 'targetRepo must be specified')
        checkNotNullOrEmpty(name, 'name must be specified')

        branches << NodeBuilder.newInstance().'hudson.plugins.git.GitPublisher_-BranchToPush' {
            targetRepoName(targetRepo)
            branchName(name)
        }
    }
}
