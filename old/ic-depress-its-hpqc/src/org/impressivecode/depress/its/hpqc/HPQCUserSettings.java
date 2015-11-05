package org.impressivecode.depress.its.hpqc;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.ext.poi.node.read2.XLSUserSettings;

/**
 * @author Łukasz Modliński, Wrocław
 * @author Mariusz Mulka, Wrocław
 * 
 */
public class HPQCUserSettings extends XLSUserSettings {

	public static final String ITS_MAPPING = "its_mapping";
	private String[] mapping;

	public HPQCUserSettings() {
		super();
	}

	public static HPQCUserSettings load(NodeSettingsRO settings)
			throws InvalidSettingsException {
		XLSUserSettings xlsSettings = XLSUserSettings.load(settings);

		HPQCUserSettings hpSettings = new HPQCUserSettings();

		hpSettings.setColHdrRow(xlsSettings.getColHdrRow());
		hpSettings.setErrorPattern(xlsSettings.getErrorPattern());
		hpSettings.setFileLocation(xlsSettings.getFileLocation());
		hpSettings.setFirstColumn(xlsSettings.getFirstColumn());
		hpSettings.setFirstRow(xlsSettings.getFirstRow());
		hpSettings.setHasColHeaders(xlsSettings.getHasColHeaders());
		hpSettings.setHasRowHeaders(xlsSettings.getHasRowHeaders());
		hpSettings.setKeepXLNames(hpSettings.getKeepXLColNames());
		hpSettings.setLastColumn(xlsSettings.getLastColumn());
		hpSettings.setLastRow(xlsSettings.getLastRow());
		hpSettings.setMissValuePattern(xlsSettings.getSheetName());
		hpSettings.setReadAllData(xlsSettings.getReadAllData());
		hpSettings.setRowHdrCol(xlsSettings.getRowHdrCol());
		hpSettings.setSheetName(xlsSettings.getSheetName());
		hpSettings.setSkipEmptyColumns(xlsSettings.getSkipEmptyColumns());
		hpSettings.setSkipEmptyRows(xlsSettings.getSkipEmptyRows());
		hpSettings.setSkipHiddenColumns(xlsSettings.getSkipHiddenColumns());
		hpSettings.setUniquifyRowIDs(xlsSettings.getUniquifyRowIDs());
		hpSettings.setUseErrorPattern(xlsSettings.getUseErrorPattern());
		hpSettings.mapping = settings.getStringArray(ITS_MAPPING);

		return hpSettings;
	}

	@Override
	public void save(NodeSettingsWO hpSettings) {
		super.save(hpSettings);
		hpSettings.addStringArray(ITS_MAPPING, mapping);
	}

	@Override
	public String getID() {
		StringBuilder id = new StringBuilder("ClearQuest Settings:");
		for (String its_map : mapping) {
			id.append(getID(its_map));
		}
		return id.toString();
	}

	public static HPQCUserSettings clone(HPQCUserSettings orig)
			throws InvalidSettingsException {
		NodeSettings clone = new NodeSettings("clone");
		orig.save(clone);
		return load(clone);
	}
    public String[] getMapping() {
        return mapping;
    }

    public void setMapping(String[] mapping) {
        this.mapping = mapping;
    }
}
