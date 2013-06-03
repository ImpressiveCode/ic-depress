package org.impressivecode.depress.scm.git;

import org.eclipse.jgit.lib.TextProgressMonitor;
import org.knime.core.node.defaultnodesettings.DialogComponentLabel;

public class LabelProgressMonitor extends TextProgressMonitor{

    private final DialogComponentLabel label;
    private final String baseText;
    
    public LabelProgressMonitor(DialogComponentLabel label, String baseText){
        this.label = label;
        this.baseText = baseText;
    }
    
    protected void onUpdate(String taskName, int workCurr){
        this.label.setText(this.baseText + taskName+" "+workCurr);
    }
    
    protected void onEndTask(String taskName, int workCurr){
        this.label.setText(this.baseText + taskName+" completed");
    }
    
    protected void onUpdate(String taskName, int cmp, int totalWork, int pcnt){
        this.label.setText(this.baseText + taskName + " " + pcnt + "% ("+cmp+"/"+totalWork+")");
    }
    
    protected void onEndTask(String taskName, int cmp, int totalWork, int pcnt){
        this.label.setText(this.baseText + taskName+" completed");
    }
}
