package org.impressivecode.depress.data.objects;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.FlowVariableModel;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

public class AnonymisationFileChooser extends DialogComponentFileChooser {

    String SelectedPath;
    
    public AnonymisationFileChooser(SettingsModelString stringModel, String historyID, String... validExtensions) {
        super(stringModel, historyID, validExtensions);
        // TODO Auto-generated constructor stub
        
        SelectedPath = stringModel.getStringValue();
        ((SettingsModelString)getModel()).setStringValue(SelectedPath);
        
        super.updateComponent();

        getModel().addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                SelectedPath = ((SettingsModelString)e.getSource()).getStringValue();
                UpdateComponent();
            }
        });
        getComponentPanel().revalidate();
    }
    
    public void UpdateComponent()
    {
        super.updateComponent();
    }
    
    public void SetSelectedPath(String newPath)
    {
        SelectedPath = newPath;
        ((SettingsModelString)getModel()).setStringValue(SelectedPath);
    }
}
