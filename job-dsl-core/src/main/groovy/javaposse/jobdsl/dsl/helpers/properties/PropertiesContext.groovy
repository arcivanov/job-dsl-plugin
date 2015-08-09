package javaposse.jobdsl.dsl.helpers.properties

import javaposse.jobdsl.dsl.ContextHelper
import javaposse.jobdsl.dsl.DslContext
import javaposse.jobdsl.dsl.Item
import javaposse.jobdsl.dsl.JobManagement
import javaposse.jobdsl.dsl.Preconditions
import javaposse.jobdsl.dsl.RequiresPlugin
import javaposse.jobdsl.dsl.helpers.AbstractExtensibleContext

class PropertiesContext extends AbstractExtensibleContext {
    List<Node> propertiesNodes = []

    PropertiesContext(JobManagement jobManagement, Item item) {
        super(jobManagement, item)
    }

    @Override
    protected void addExtensionNode(Node node) {
        propertiesNodes << node
    }

    /**
     * Adds links in the sidebar of the project page.
     *
     * @since 1.33
     */
    @RequiresPlugin(id = 'sidebar-link', minimumVersion = '1.7')
    void sidebarLinks(@DslContext(SidebarLinkContext) Closure sidebarLinkClosure) {
        SidebarLinkContext sidebarLinkContext = new SidebarLinkContext()
        ContextHelper.executeInContext(sidebarLinkClosure, sidebarLinkContext)

        propertiesNodes << new NodeBuilder().'hudson.plugins.sidebar__link.ProjectLinks' {
            links(sidebarLinkContext.links)
        }
    }

    /**
     * Allows to configure a custom icon for each job.
     *
     * @since 1.33
     */
    @RequiresPlugin(id = 'custom-job-icon', minimumVersion = '0.2')
    void customIcon(String iconFileName) {
        Preconditions.checkNotNullOrEmpty(iconFileName, 'iconFileName must be specified')

        propertiesNodes << new NodeBuilder().'jenkins.plugins.jobicon.CustomIconProperty' {
            iconfile(iconFileName)
        }
    }
}
