package javaposse.jobdsl.dsl

import static javaposse.jobdsl.dsl.Preconditions.checkArgument

class BuildBlockerContext extends AbstractContext {
    private static final Set<String> VALID_BLOCK_LEVELS = ['GLOBAL', 'NODE']
    private static final Set<String> VALID_QUEUE_SCAN_SCOPES = ['ALL', 'BUILDABLE', 'DISABLED']

    String blockLevel = 'NODE'
    String scanQueueFor = 'DISABLED'

    protected BuildBlockerContext(JobManagement jobManagement) {
        super(jobManagement)
    }

    @RequiresPlugin(id = 'build-blocker-plugin', minimumVersion = '1.7.1')
    void blockLevel(String blockLevel) {
        checkArgument(
                VALID_BLOCK_LEVELS.contains(blockLevel),
                "blockLevel must be one of ${VALID_BLOCK_LEVELS.join(', ')}"
        )
        this.blockLevel = blockLevel
    }

    @RequiresPlugin(id = 'build-blocker-plugin', minimumVersion = '1.7.1')
    void scanQueueFor(String scanQueueFor) {
        checkArgument(
                VALID_QUEUE_SCAN_SCOPES.contains(scanQueueFor),
                "scanQueueFor must be one of ${VALID_QUEUE_SCAN_SCOPES.join(', ')}"
        )
        this.scanQueueFor = scanQueueFor
    }
}
