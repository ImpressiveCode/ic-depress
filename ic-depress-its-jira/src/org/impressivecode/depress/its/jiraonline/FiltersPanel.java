package org.impressivecode.depress.its.jiraonline;

import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsWO;

import pl.enofod.shuttlelist.ShuttleList;

public class FiltersPanel extends NodeDialogPane {

	private static final long serialVersionUID = 8088597236737829326L;

	public FiltersPanel() {
		
		ShuttleList<String> list = new ShuttleList<>();
		list.addElement("test1");
		list.addElement("test2");
		
		//this.
		
	//	this.add(list);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
		
	}

}
