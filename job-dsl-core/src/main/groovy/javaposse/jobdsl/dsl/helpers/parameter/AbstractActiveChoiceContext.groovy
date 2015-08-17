package javaposse.jobdsl.dsl.helpers.parameter

import javaposse.jobdsl.dsl.Context
import javaposse.jobdsl.dsl.DslContext

import static javaposse.jobdsl.dsl.ContextHelper.executeInContext

abstract class AbstractActiveChoiceContext implements Context {
    String description
    Node script

    void description(String description) {
        this.description = description
    }

    void groovyScript(@DslContext(ActiveChoiceGroovyScriptContext) Closure closure) {
        ActiveChoiceGroovyScriptContext context = new ActiveChoiceGroovyScriptContext()
        executeInContext(closure, context)

        script = new NodeBuilder().script(class: 'org.biouno.unochoice.model.GroovyScript') {
            delegate.script(context.script ?: '')
            delegate.fallbackScript(context.fallbackScript ?: '')
        }
    }

    void scriptlerScript(String scriptId, @DslContext(ActiveChoiceScriptlerScriptContext) Closure closure = null) {
        ActiveChoiceScriptlerScriptContext context = new ActiveChoiceScriptlerScriptContext()
        executeInContext(closure, context)

        script = new NodeBuilder().script(class: 'org.biouno.unochoice.model.ScriptlerScript') {
            scriptlerScriptId(scriptId)
            parameters {
                context.parameters.each { String name, String value ->
                    entry {
                        string(name)
                        string(value)
                    }
                }
            }
        }
    }
}
