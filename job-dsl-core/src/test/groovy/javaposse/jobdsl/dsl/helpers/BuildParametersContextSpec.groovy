package javaposse.jobdsl.dsl.helpers

import javaposse.jobdsl.dsl.JobManagement
import spock.lang.Specification

class BuildParametersContextSpec extends Specification {
    JobManagement jobManagement = Mock(JobManagement)
    BuildParametersContext context = new BuildParametersContext(jobManagement)

    def 'base booleanParam usage'() {
        when:
        context.booleanParam('myParameterName', true, 'myBooleanParameterDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'true'
        context.buildParameterNodes['myParameterName'].description.text() == 'myBooleanParameterDescription'
    }

    def 'simplified booleanParam usage'() {
        when:
        context.booleanParam('myParameterName', true)

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'true'
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'simplest booleanParam usage'() {
        when:
        context.booleanParam('myParameterName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'false'
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'booleanParam name argument cant be null'() {
        when:
        context.booleanParam(null, false)

        then:
        thrown(NullPointerException)
    }

    def 'booleanParam name argument cant be empty'() {
        when:
        context.booleanParam('', false)

        then:
        thrown(IllegalArgumentException)
    }

    def 'multiple booleanParams is just fine'() {
        when:
        context.booleanParam('myFirstBooleanParameter')
        context.booleanParam('mySecondBooleanParameter')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 2
        context.buildParameterNodes['myFirstBooleanParameter'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['myFirstBooleanParameter'].name.text() == 'myFirstBooleanParameter'
        context.buildParameterNodes['myFirstBooleanParameter'].defaultValue.text() == 'false'
        context.buildParameterNodes['myFirstBooleanParameter'].description.text() == ''
        context.buildParameterNodes['mySecondBooleanParameter'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['mySecondBooleanParameter'].name.text() == 'mySecondBooleanParameter'
        context.buildParameterNodes['mySecondBooleanParameter'].defaultValue.text() == 'false'
        context.buildParameterNodes['mySecondBooleanParameter'].description.text() == ''
    }

    def 'booleanParam already defined'() {
        when:
        context.booleanParam('one')
        context.booleanParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'base listTagsParam usage'() {
        when:
        context.listTagsParam('myParameterName', 'http://kenai.com/svn/myProject/tags', '^mytagsfilterregex', true,
                true, 'maximumNumberOfTagsToDisplay', 'theDefaultValue', 'myListTagsParameterDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition'
            name.text() == 'myParameterName'
            defaultValue.text() == 'theDefaultValue'
            tagsDir.text() == 'http://kenai.com/svn/myProject/tags'
            tagsFilter.text() == '^mytagsfilterregex'
            reverseByDate.text() == 'true'
            reverseByName.text() == 'true'
            maxTags.text() == 'maximumNumberOfTagsToDisplay'
            description.text() == 'myListTagsParameterDescription'
        }
        1 * jobManagement.requirePlugin('subversion')
    }

    def 'simplified listTagsParam usage'() {
        when:
        context.listTagsParam('myParameterName', 'http://kenai.com/svn/myProject/tags', '^mytagsfilterregex', true,
                true)

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition'
            name.text() == 'myParameterName'
            tagsDir.text() == 'http://kenai.com/svn/myProject/tags'
            tagsFilter.text() == '^mytagsfilterregex'
            reverseByDate.text() == 'true'
            reverseByName.text() == 'true'
            maxTags.text() == 'all'
        }
        1 * jobManagement.requirePlugin('subversion')
    }

    def 'simplest listTagsParam usage'() {
        when:
        context.listTagsParam('myParameterName', 'http://kenai.com/svn/myProject/tags', '^mytagsfilterregex')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition'
            name.text() == 'myParameterName'
            tagsDir.text() == 'http://kenai.com/svn/myProject/tags'
            tagsFilter.text() == '^mytagsfilterregex'
            reverseByDate.text() == 'false'
            reverseByName.text() == 'false'
            maxTags.text() == 'all'
        }
        1 * jobManagement.requirePlugin('subversion')
    }

    def 'listTagsParam name argument cant be null'() {
        when:
        context.listTagsParam(null, 'http://kenai.com/svn/myProject/tags', '^mytagsfilterregex')

        then:
        thrown(NullPointerException)
    }

    def 'listTagsParam name argument cant be empty'() {
        when:
        context.listTagsParam('', 'http://kenai.com/svn/myProject/tags', '^mytagsfilterregex')

        then:
        thrown(IllegalArgumentException)
    }

    def 'listTagsParam scmUrl argument cant be null'() {
        when:
        context.listTagsParam('myParameterName', null, '^mytagsfilterregex')

        then:
        thrown(NullPointerException)
    }

    def 'listTagsParam scmUrl argument cant be empty'() {
        when:
        context.listTagsParam('myParameterName', '', '^mytagsfilterregex')

        then:
        thrown(IllegalArgumentException)
    }

    def 'listTagsParam tagFilterRegex argument can be null or empty'(String filter) {
        when:
        context.listTagsParam('myParameterName', 'http://kenai.com/svn/myProject/tags', filter)

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'hudson.scm.listtagsparameter.ListSubversionTagsParameterDefinition'
            name.text() == 'myParameterName'
            tagsDir.text() == 'http://kenai.com/svn/myProject/tags'
            tagsFilter.text() == ''
            reverseByDate.text() == 'false'
            reverseByName.text() == 'false'
            maxTags.text() == 'all'
        }
        1 * jobManagement.requirePlugin('subversion')

        where:
        filter << [null, '']
    }

    def 'listTagsParam already defined'() {
        when:
        context.booleanParam('one')
        context.listTagsParam('one', 'http://kenai.com/svn/myProject/tags', '')

        then:
        thrown(IllegalArgumentException)
    }

    def 'base choiceParam usage'() {
        when:
        context.choiceParam('myParameterName', ['option 1 (default)', 'option 2'], 'myChoiceParamDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.ChoiceParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].description.text() == 'myChoiceParamDescription'
        context.buildParameterNodes['myParameterName'].choices.size() == 1
        context.buildParameterNodes['myParameterName'].choices[0].attribute('class') == 'java.util.Arrays$ArrayList'
        context.buildParameterNodes['myParameterName'].choices[0].a.size() == 1
        context.buildParameterNodes['myParameterName'].choices[0].a[0].attribute('class') == 'string-array'
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string.size() == 2
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string[0].text() == 'option 1 (default)'
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string[1].text() == 'option 2'
    }

    def 'simplified choiceParam usage'() {
        when:
        context.choiceParam('myParameterName', ['option 1 (default)', 'option 2', 'option 3'])

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.ChoiceParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].description.text() == ''
        context.buildParameterNodes['myParameterName'].choices.size() == 1
        context.buildParameterNodes['myParameterName'].choices[0].attribute('class') == 'java.util.Arrays$ArrayList'
        context.buildParameterNodes['myParameterName'].choices[0].a.size() == 1
        context.buildParameterNodes['myParameterName'].choices[0].a[0].attribute('class') == 'string-array'
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string.size() == 3
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string[0].text() == 'option 1 (default)'
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string[1].text() == 'option 2'
        context.buildParameterNodes['myParameterName'].choices[0].a[0].string[2].text() == 'option 3'
    }

    def 'choiceParam name argument cant be null'() {
        when:
        context.choiceParam(null, ['option 1'])

        then:
        thrown(NullPointerException)
    }

    def 'choiceParam name argument cant be empty'() {
        when:
        context.choiceParam('', ['option 1'])

        then:
        thrown(IllegalArgumentException)
    }

    def 'choiceParam options argument cant be null'() {
        when:
        context.choiceParam('myParameterName', null)

        then:
        thrown(NullPointerException)
    }

    def 'choiceParam options argument cant be empty'() {
        when:
        context.choiceParam('myParameterName', [])

        then:
        thrown(IllegalArgumentException)
    }

    def 'choiceParam already defined'() {
        when:
        context.booleanParam('one')
        context.choiceParam('one', ['foo', 'bar'])

        then:
        thrown(IllegalArgumentException)
    }

    def 'base fileParam usage'() {
        when:
        context.fileParam('test/upload.zip', 'myFileParamDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['test/upload.zip'].name() == 'hudson.model.FileParameterDefinition'
        context.buildParameterNodes['test/upload.zip'].name.text() == 'test/upload.zip'
        context.buildParameterNodes['test/upload.zip'].description.text() == 'myFileParamDescription'
    }

    def 'simplified fileParam usage'() {
        when:
        context.fileParam('test/upload.zip')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['test/upload.zip'].name() == 'hudson.model.FileParameterDefinition'
        context.buildParameterNodes['test/upload.zip'].name.text() == 'test/upload.zip'
        context.buildParameterNodes['test/upload.zip'].description.text() == ''
    }

    def 'fileParam fileLocation argument cant be null'() {
        when:
        context.fileParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'fileParam fileLocation argument cant be empty'() {
        when:
        context.fileParam('')

        then:
        thrown(IllegalArgumentException)
    }

    def 'fileParam already defined'() {
        when:
        context.booleanParam('one')
        context.fileParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'base runParam usage'() {
        when:
        context.runParam('myParameterName', 'myJobName', 'myRunParamDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.RunParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].projectName.text() == 'myJobName'
        context.buildParameterNodes['myParameterName'].description.text() == 'myRunParamDescription'
    }

    def 'simplest runParam usage'() {
        when:
        context.runParam('myParameterName', 'myJobName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.RunParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].projectName.text() == 'myJobName'
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'fullest runParam usage'() {
        when:
        context.runParam('myParameterName', 'myJobName', 'my description with spaces', 'SUCCESSFUL')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.RunParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].projectName.text() == 'myJobName'
        context.buildParameterNodes['myParameterName'].description.text() == 'my description with spaces'
        context.buildParameterNodes['myParameterName'].filter.text() == 'SUCCESSFUL'
    }

    def 'runParam name argument cant be null'() {
        when:
        context.runParam(null, null)

        then:
        thrown(NullPointerException)
    }

    def 'runParam name argument cant be empty'() {
        when:
        context.runParam('', '')

        then:
        thrown(IllegalArgumentException)
    }

    def 'runParam jobToRun argument cant be null'() {
        when:
        context.runParam('myParameterName', null)

        then:
        thrown(NullPointerException)
    }

    def 'runParam jobToRun argument cant be empty'() {
        when:
        context.runParam('myParameterName', '')

        then:
        thrown(IllegalArgumentException)
    }

    def 'runParam already defined'() {
        when:
        context.booleanParam('one')
        context.runParam('one', 'job')

        then:
        thrown(IllegalArgumentException)
    }

    def 'base stringParam usage'() {
        when:
        context.stringParam('myParameterName', 'my default stringParam value', 'myStringParameterDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.StringParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'my default stringParam value'
        context.buildParameterNodes['myParameterName'].description.text() == 'myStringParameterDescription'
    }

    def 'simplified stringParam usage'() {
        when:
        context.stringParam('myParameterName', 'my default stringParam value')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.StringParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'my default stringParam value'
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'simplest stringParam usage'() {
        when:
        context.stringParam('myParameterName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.StringParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == ''
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'stringParam name argument cant be null'() {
        when:
        context.stringParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'stringParam name argument cant be empty'() {
        when:
        context.stringParam('')

        then:
        thrown(IllegalArgumentException)
    }

    def 'stringParam already defined'() {
        when:
        context.booleanParam('one')
        context.stringParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'base textParam usage'() {
        when:
        context.textParam('myParameterName', 'my default textParam value', 'myTextParameterDescription')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.TextParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'my default textParam value'
        context.buildParameterNodes['myParameterName'].description.text() == 'myTextParameterDescription'
    }

    def 'simplified textParam usage'() {
        when:
        context.textParam('myParameterName', 'my default textParam value')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.TextParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == 'my default textParam value'
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'simplest textParam usage'() {
        when:
        context.textParam('myParameterName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        context.buildParameterNodes['myParameterName'].name() == 'hudson.model.TextParameterDefinition'
        context.buildParameterNodes['myParameterName'].name.text() == 'myParameterName'
        context.buildParameterNodes['myParameterName'].defaultValue.text() == ''
        context.buildParameterNodes['myParameterName'].description.text() == ''
    }

    def 'textParam name argument cant be null'() {
        when:
        context.textParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'textParam name argument cant be empty'() {
        when:
        context.textParam('')

        then:
        thrown(IllegalArgumentException)
    }

    def 'textParam already defined'() {
        when:
        context.booleanParam('one')
        context.textParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'nodeParam base usage'() {
        when:
        context.nodeParam('myParameterName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.NodeParameterDefinition'
            children().size() == 9
            name[0].value() == 'myParameterName'
            allowedSlaves[0].children().size() == 0
            defaultSlaves[0].children().size() == 0
            description[0].value() == null
            triggerIfResult[0].value() == 'multiSelectionDisallowed'
            nodeEligibility[0].attribute('class') ==
                    'org.jvnet.jenkins.plugins.nodelabelparameter.node.AllNodeEligibility'
            allowMultiNodeSelection[0].value() == false
            triggerConcurrentBuilds[0].value() == false
            ignoreOfflineNodes[0].value() == false
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'nodeParam fullest usage'() {
        when:
        context.nodeParam('myParameterName') {
            description('myRunParamDescription')
            allowedNodes(['myNode', 'myNode2'])
            defaultNodes(['myNode'])
            trigger('multiSelectionDisallowed')
            eligibility('IgnoreOfflineNodeEligibility')
        }

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.NodeParameterDefinition'
            children().size() == 9
            name[0].value() == 'myParameterName'
            allowedSlaves[0].string.size() == 2
            allowedSlaves[0].string[0].value() == 'myNode'
            allowedSlaves[0].string[1].value() == 'myNode2'
            defaultSlaves[0].string.size() == 1
            defaultSlaves[0].string[0].value() == 'myNode'
            description[0].value() == 'myRunParamDescription'
            triggerIfResult[0].value() == 'multiSelectionDisallowed'
            nodeEligibility[0].attribute('class') ==
                 'org.jvnet.jenkins.plugins.nodelabelparameter.node.IgnoreOfflineNodeEligibility'
            allowMultiNodeSelection[0].value() == false
            triggerConcurrentBuilds[0].value() == false
            ignoreOfflineNodes[0].value() == false
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'nodeParam name argument cant be null'() {
        when:
        context.nodeParam(null, null)

        then:
        thrown(NullPointerException)
    }

    def 'nodeParam invalid trigger'() {
        when:
        context.nodeParam('myParamName') {
            trigger('invalid trigger')
        }

        then:
        thrown(IllegalArgumentException)
    }

    def 'nodeParam no name'() {
        when:
        context.nodeParam('')

        then:
        thrown(IllegalArgumentException)

        when:
        context.nodeParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'nodeParam already defined'() {
        when:
        context.booleanParam('one')
        context.nodeParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'labelParam base usage'() {
        when:
        context.labelParam('myParameterName') {
            defaultValue('myDefaultValue')
            description('myRunParamDescription')
        }

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.LabelParameterDefinition'
            children().size() == 6
            name.text() == 'myParameterName'
            defaultValue.text() == 'myDefaultValue'
            description.text() == 'myRunParamDescription'
            allNodesMatchingLabel.text() == 'false'
            triggerIfResult.text() == 'allCases'
            nodeEligibility.size() == 1
            nodeEligibility[0].@class == 'org.jvnet.jenkins.plugins.nodelabelparameter.node.AllNodeEligibility'
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'labelParam simplest usage'() {
        when:
        context.labelParam('myParameterName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.LabelParameterDefinition'
            children().size() == 6
            name.text() == 'myParameterName'
            defaultValue.text() == ''
            description.text() == ''
            allNodesMatchingLabel.text() == 'false'
            triggerIfResult.text() == 'allCases'
            nodeEligibility.size() == 1
            nodeEligibility[0].@class == 'org.jvnet.jenkins.plugins.nodelabelparameter.node.AllNodeEligibility'
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'labelParam run on all nodes'() {
        when:
        context.labelParam('myParameterName') {
            allNodes()
        }

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.LabelParameterDefinition'
            children().size() == 6
            name.text() == 'myParameterName'
            allNodesMatchingLabel.text() == 'true'
            defaultValue.text() == ''
            description.text() == ''
            triggerIfResult.text() == 'allCases'
            nodeEligibility.size() == 1
            nodeEligibility[0].@class == 'org.jvnet.jenkins.plugins.nodelabelparameter.node.AllNodeEligibility'
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'labelParam allNodes accepts valid values only'(String trigger, String eligibility) {
        when:
        context.labelParam('myParameterName') {
            allNodes(trigger, eligibility)
        }

        then:
        thrown(IllegalArgumentException)

        where:
        trigger    | eligibility
        'allCases' | 'what?'
        'what?'    | 'AllNodeEligibility?'
    }

    def 'labelParam fullest usage'() {
        when:
        context.labelParam('myParameterName') {
            defaultValue('myDefaultValue')
            description('myRunParamDescription')
            allNodes('success', 'IgnoreOfflineNodeEligibility')
        }

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['myParameterName']) {
            name() == 'org.jvnet.jenkins.plugins.nodelabelparameter.LabelParameterDefinition'
            children().size() == 6
            name.text() == 'myParameterName'
            defaultValue.text() == 'myDefaultValue'
            description.text() == 'myRunParamDescription'
            allNodesMatchingLabel.text() == 'true'
            triggerIfResult.text() == 'success'
            nodeEligibility.size() == 1
            nodeEligibility[0].@class ==
                    'org.jvnet.jenkins.plugins.nodelabelparameter.node.IgnoreOfflineNodeEligibility'
        }
        1 * jobManagement.requirePlugin('nodelabelparameter')
    }

    def 'labelParam name argument cant be null'() {
        when:
        context.labelParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'labelParam name argument cant be empty'() {
        when:
        context.labelParam('')

        then:
        thrown(IllegalArgumentException)
    }

    def 'labelParam already defined'() {
        when:
        context.booleanParam('one')
        context.labelParam('one')

        then:
        thrown(IllegalArgumentException)
    }

    def 'gitParam already defined'() {
        when:
        context.stringParam('paramName')
        context.gitParam('paramName')

        then:
        thrown(IllegalArgumentException)
    }

    def 'gitParam name argument cant be null'() {
        when:
        context.gitParam(null)

        then:
        thrown(NullPointerException)
    }

    def 'gitParam name argument cant be empty'() {
        when:
        context.gitParam('')

        then:
        thrown(IllegalArgumentException)
    }

    def 'gitParam minimal options'() {
        when:
        context.gitParam('paramName')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['paramName']) {
            name() == 'net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterDefinition'
            children().size() == 8
            name.text() == 'paramName'
            description[0].value() == ''
            UUID.fromString(uuid[0].value() as String)
            type[0].value() == 'PT_TAG'
            branch[0].value() == ''
            tagFilter[0].value() == ''
            sortMode[0].value() == 'NONE'
            defaultValue[0].value() == ''
        }
        1 * jobManagement.requireMinimumPluginVersion('git-parameter', '0.4.0')
    }

    def 'gitParam all options'() {
        when:
        context.gitParam('sha') {
            description('Revision commit SHA')
            type('REVISION')
            branch('master')
            tagFilter('*')
            sortMode('ASCENDING_SMART')
            defaultValue('foo')
        }

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 1
        with(context.buildParameterNodes['sha']) {
            name() == 'net.uaznia.lukanus.hudson.plugins.gitparameter.GitParameterDefinition'
            children().size() == 8
            name.text() == 'sha'
            description[0].value() == 'Revision commit SHA'
            UUID.fromString(uuid[0].value() as String)
            type[0].value() == 'PT_REVISION'
            branch[0].value() == 'master'
            tagFilter[0].value() == '*'
            sortMode[0].value() == 'ASCENDING_SMART'
            defaultValue[0].value() == 'foo'
        }
        1 * jobManagement.requireMinimumPluginVersion('git-parameter', '0.4.0')
    }

    def 'multiple mixed Param types is just fine'() {
        when:
        context.booleanParam('myFirstBooleanParameter')
        context.textParam('myFirstTextParam')
        context.booleanParam('mySecondBooleanParameter')

        then:
        context.buildParameterNodes != null
        context.buildParameterNodes.size() == 3
        context.buildParameterNodes['myFirstBooleanParameter'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['myFirstBooleanParameter'].name.text() == 'myFirstBooleanParameter'
        context.buildParameterNodes['myFirstBooleanParameter'].defaultValue.text() == 'false'
        context.buildParameterNodes['myFirstBooleanParameter'].description.text() == ''
        context.buildParameterNodes['myFirstTextParam'].name() == 'hudson.model.TextParameterDefinition'
        context.buildParameterNodes['myFirstTextParam'].name.text() == 'myFirstTextParam'
        context.buildParameterNodes['myFirstTextParam'].defaultValue.text() == ''
        context.buildParameterNodes['myFirstTextParam'].description.text() == ''
        context.buildParameterNodes['mySecondBooleanParameter'].name() == 'hudson.model.BooleanParameterDefinition'
        context.buildParameterNodes['mySecondBooleanParameter'].name.text() == 'mySecondBooleanParameter'
        context.buildParameterNodes['mySecondBooleanParameter'].defaultValue.text() == 'false'
        context.buildParameterNodes['mySecondBooleanParameter'].description.text() == ''
    }
}
