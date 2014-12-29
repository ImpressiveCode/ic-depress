/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.impressivecode.depress.mr.intellijmetrics;

import static com.google.common.base.Preconditions.checkArgument;
import static org.impressivecode.depress.mr.intellijmetrics.IntellijMetricsTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.xml.sax.SAXException;

/**
 *
 * @author Maciej Mickiewicz, Wroclaw University of Technology
 *
 */
public class IntellijMetricsNodeModel extends NodeModel {

  private static final NodeLogger LOGGER = NodeLogger
      .getLogger(IntellijMetricsNodeModel.class);

  static final String FILE = "File";
  static final String DIRECTORY = "Directory";
  static final String[] actions = { FILE, DIRECTORY };
  static final String RADIO_DEFAULT = "File";
  static final String FILE_NAME_CONFIG_XML = "fileXML";
  static final String FILE_NAME_CONFIG_DIR = "fileDIR";
  static final String FILE_DEFAULT_VALUE_DIR = "";
  static final String FILE_DEFAULT_VALUE_XML = "";
  static final String RADIO_CONFIG = "radio";

  private final SettingsModelString fileSettingsXML = createFileSettingsXML();
  private final SettingsModelString fileSettingsDIR = createFileSettingsDIR();
  private final SettingsModelString radioSettings = createRadioSettings();

  protected IntellijMetricsNodeModel() {
    super(0, 1);
  }

  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws Exception {
    LOGGER.info("Preparing to read Intellij entries.");
    File file = null;
    if (radioSettings.getStringValue().equals(FILE)) {
      file = new File(fileSettingsXML.getStringValue());
    } else if (radioSettings.getStringValue().equals(DIRECTORY)) {
      file = new File(fileSettingsDIR.getStringValue());
    }

    List<IntellijMetricsEntry> entries;
    if (file.isDirectory()) {
      entries = parseEntriesFromPath(fileSettingsDIR.getStringValue());
    } else if (file.isFile()) {
      entries = parseEntriesFromPath(fileSettingsXML.getStringValue());
    } else
      throw new IOException(
          "Path does not point at any file or directory");

    LOGGER.info("Transforming to Intellij entries.");
    BufferedDataTable out = transform(entries, exec);
    LOGGER.info("Intellij table created.");
    return new BufferedDataTable[] { out };
  }

  private BufferedDataTable transform(final List<IntellijMetricsEntry> entries,
      final ExecutionContext exec) throws CanceledExecutionException {
    IntellijMetricsTransformer transformer = new IntellijMetricsTransformer(
        createDataColumnSpec());
    return transformer.transform(entries, exec);
  }

  private List<IntellijMetricsEntry> parseEntriesFromPath(final String filePath)
      throws ParserConfigurationException, SAXException, IOException {
    IntellijMetricsEntriesParser parser = new IntellijMetricsEntriesParser();
    return parser.parseEntries(filePath);
  }

  @Override
  protected void reset() {
  }

  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    checkArgument(inSpecs.length == 0, "Invalid state");
    return new DataTableSpec[] { createDataColumnSpec() };
  }

  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    fileSettingsXML.saveSettingsTo(settings);
    fileSettingsDIR.saveSettingsTo(settings);
    radioSettings.saveSettingsTo(settings);
  }

  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    fileSettingsXML.loadSettingsFrom(settings);
    fileSettingsDIR.loadSettingsFrom(settings);
    radioSettings.loadSettingsFrom(settings);
  }

  @Override
  protected void validateSettings(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    fileSettingsXML.validateSettings(settings);
    fileSettingsDIR.validateSettings(settings);
    radioSettings.validateSettings(settings);
  }

  @Override
  protected void loadInternals(final File internDir,
      final ExecutionMonitor exec) throws IOException,
      CanceledExecutionException {
    // NOOP
  }

  @Override
  protected void saveInternals(final File internDir,
      final ExecutionMonitor exec) throws IOException,
      CanceledExecutionException {
    // NOOP
  }

  static SettingsModelString createFileSettingsDIR() {
    return new SettingsModelString(FILE_NAME_CONFIG_DIR,
        FILE_DEFAULT_VALUE_DIR);
  }

  static SettingsModelString createFileSettingsXML() {
    return new SettingsModelString(FILE_NAME_CONFIG_XML,
        FILE_DEFAULT_VALUE_XML);
  }

  static SettingsModelString createRadioSettings() {
    return new SettingsModelString(RADIO_CONFIG, RADIO_DEFAULT);
  }
}
