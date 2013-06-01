package org.impressivecode.depress.scm.git;

import org.eclipse.jgit.lib.TextProgressMonitor;
import org.knime.core.node.NodeLogger;

public class NodeLoggerProgressMonitor extends TextProgressMonitor{

    private final NodeLogger logger;
    
    public NodeLoggerProgressMonitor(NodeLogger logger){
        this.logger = logger;
    }
    
    protected void onUpdate(String taskName, int workCurr){
        this.logger.info(taskName+": "+workCurr);
    }
    
    protected void onEndTask(String taskName, int workCurr){
        this.logger.info(taskName+" completed");
    }
    
    protected void onUpdate(String taskName, int cmp, int totalWork, int pcnt){
        this.logger.info(taskName + ": " + pcnt + "% ("+cmp+"/"+totalWork+")");
    }
    
    protected void onEndTask(String taskName, int cmp, int totalWork, int pcnt){
        this.logger.info(taskName+" completed");
    }
}
