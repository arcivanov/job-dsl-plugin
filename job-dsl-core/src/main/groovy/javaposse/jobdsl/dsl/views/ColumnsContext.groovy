package javaposse.jobdsl.dsl.views

import javaposse.jobdsl.dsl.AbstractContext
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.RequiresPlugin

class ColumnsContext extends AbstractContext {
    List<Node> columnNodes = []

    ColumnsContext(JobManagement jobManagement) {
        super(jobManagement)
    }

    void status() {
        columnNodes << new Node(null, 'hudson.views.StatusColumn')
    }

    void weather() {
        columnNodes << new Node(null, 'hudson.views.WeatherColumn')
    }

    void name() {
        columnNodes << new Node(null, 'hudson.views.JobColumn')
    }

    void lastSuccess() {
        columnNodes << new Node(null, 'hudson.views.LastSuccessColumn')
    }

    void lastFailure() {
        columnNodes << new Node(null, 'hudson.views.LastFailureColumn')
    }

    void lastDuration() {
        columnNodes << new Node(null, 'hudson.views.LastDurationColumn')
    }

    void buildButton() {
        columnNodes << new Node(null, 'hudson.views.BuildButtonColumn')
    }

    /**
     * @since 1.23
     */
    @RequiresPlugin(id = 'extra-columns')
    void lastBuildConsole() {
        columnNodes << new Node(null, 'jenkins.plugins.extracolumns.LastBuildConsoleColumn')
    }

    /**
     * @since 1.31
     */
    @RequiresPlugin(id = 'extra-columns')
    void configureProject() {
        columnNodes << new Node(null, 'jenkins.plugins.extracolumns.ConfigureProjectColumn')
    }

    /**
     * @since 1.29
     */
    @RequiresPlugin(id = 'claim')
    void claim() {
        columnNodes << new Node(null, 'hudson.plugins.claim.ClaimColumn')
    }

    /**
     * @since 1.31
     */
    @RequiresPlugin(id = 'build-node-column', minimumVersion = '0.1')
    void lastBuildNode() {
        columnNodes << new Node(null, 'org.jenkins.plugins.column.LastBuildNodeColumn')
    }

    /**
     * @since 1.31
     */
    @RequiresPlugin(id = 'categorized-view', minimumVersion = '1.8')
    void categorizedJob() {
        columnNodes << new Node(null, 'org.jenkinsci.plugins.categorizedview.IndentedJobColumn')
    }

    /**
     * @since 1.33
     */
    @RequiresPlugin(id = 'robot', minimumVersion = '1.6.0')
    void robotResults() {
        columnNodes << new Node(null, 'hudson.plugins.robot.view.RobotListViewColumn')
    }

    /**
     * @since 1.33
     */
    @RequiresPlugin(id = 'custom-job-icon', minimumVersion = '0.2')
    void customIcon() {
        columnNodes << new Node(null, 'jenkins.plugins.jobicon.CustomIconColumn')
    }
}
