package org.impressivecode.depress.its.clearquest;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.ext.poi.node.read2.XLSUserSettings;

/**
 * 
 * @author Łukasz Leśniczek, Wrocław
 * 
 */
public class ClearQuestUserSettings extends XLSUserSettings {

    public static final String ITS_MAPPING = "its_mapping";
    private String[] mapping;

    public ClearQuestUserSettings() {
        super();
    }

    public static ClearQuestUserSettings load(NodeSettingsRO settings) throws InvalidSettingsException {
        XLSUserSettings xlsSettings = XLSUserSettings.load(settings);

        ClearQuestUserSettings cqSettings = new ClearQuestUserSettings();
        cqSettings.setColHdrRow(xlsSettings.getColHdrRow());
        cqSettings.setErrorPattern(xlsSettings.getErrorPattern());
        cqSettings.setFileLocation(xlsSettings.getFileLocation());
        cqSettings.setFirstColumn(xlsSettings.getFirstColumn());
        cqSettings.setFirstRow(xlsSettings.getFirstRow());
        cqSettings.setHasColHeaders(xlsSettings.getHasColHeaders());
        cqSettings.setHasRowHeaders(xlsSettings.getHasRowHeaders());
        cqSettings.setKeepXLNames(cqSettings.getKeepXLColNames());
        cqSettings.setLastColumn(xlsSettings.getLastColumn());
        cqSettings.setLastRow(xlsSettings.getLastRow());
        cqSettings.setMissValuePattern(xlsSettings.getSheetName());
        cqSettings.setReadAllData(xlsSettings.getReadAllData());
        cqSettings.setRowHdrCol(xlsSettings.getRowHdrCol());
        cqSettings.setSheetName(xlsSettings.getSheetName());
        cqSettings.setSkipEmptyColumns(xlsSettings.getSkipEmptyColumns());
        cqSettings.setSkipEmptyRows(xlsSettings.getSkipEmptyRows());
        cqSettings.setSkipHiddenColumns(xlsSettings.getSkipHiddenColumns());
        cqSettings.setUniquifyRowIDs(xlsSettings.getUniquifyRowIDs());
        cqSettings.setUseErrorPattern(xlsSettings.getUseErrorPattern());
        cqSettings.mapping = settings.getStringArray(ITS_MAPPING);

        return cqSettings;
    }

    @Override
    public void save(NodeSettingsWO cqSettings) {
        super.save(cqSettings);

        cqSettings.addStringArray(ITS_MAPPING, mapping);
    }

    @Override
    public String getID() {
        StringBuilder id = new StringBuilder("ClearQuest Settings:");
        for (String its_map : mapping) {
            id.append(getID(its_map));
        }
        return id.toString();
    }

    public static ClearQuestUserSettings clone(ClearQuestUserSettings orig) throws InvalidSettingsException {
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
