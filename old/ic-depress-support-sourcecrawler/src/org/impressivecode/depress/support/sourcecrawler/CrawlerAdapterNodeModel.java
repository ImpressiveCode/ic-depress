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
package org.impressivecode.depress.support.sourcecrawler;

import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createTableRow;
import static org.impressivecode.depress.support.sourcecrawler.CrawlerAdapterTableFactory.createTableSpec;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class CrawlerAdapterNodeModel extends NodeModel {
	static final String FILE = "File";
	static final String DIRECTORY = "Directory";
	static final String[] actions = { FILE, DIRECTORY };

	static final String FILE_DEFAULT_VALUE_DIR = "";
	static final String FILE_DEFAULT_VALUE_XML = "";
	private static final String PACKAGE_DEFAULT_VALUE = "org.";

	static final String PUBLIC = "Public";
	static final String PRIVATE = "Private";
	static final String PROTECTED = "Protected";
	static final String PACKAGE = "Package-private";
	static final String CLASS = "Class";
	static final String INTERFACE = "Interface";
	static final String ABSTRACT = "Abstract";
	static final String ENUM = "Enum";
	static final String EXCEPTION = "Exception";
	static final String INNER = "Inner";
	static final String TEST = "Test";
	static final String FINAL = "Final";
	static final String CREATE_XML = "Create XML file";
	static final String RADIO_DEFAULT = "File";

	static final String PACKAGE_NAME_CONFIG = "package";
	static final String CREATE_XML_CONFIG = "create xml";
	static final String PUBLIC_CONFIG = "public";
	static final String PRIVATE_CONFIG = "private";
	static final String PROTECTED_CONFIG = "protected";
	static final String PACKAGE_CONFIG = "packageprivate";
	static final String CLASS_CONFIG = "class";
	static final String INTERFACE_CONFIG = "interface";
	static final String ABSTRACT_CONFIG = "abstract";
	static final String ENUM_CONFIG = "enum";
	static final String EXCEPTION_CONFIG = "exception";
	static final String INNER_CONFIG = "inner";
	static final String TEST_CONFIG = "test";
	static final String FINAL_CONFIG = "final";
	static final String RADIO_CONFIG = "radio";
	static final String FILE_NAME_CONFIG_XML = "fileXML";
	static final String FILE_NAME_CONFIG_DIR = "fileDIR";

	private final SettingsModelString fileSettingsXML = createFileSettingsXML();
	private final SettingsModelString fileSettingsDIR = createFileSettingsDIR();
	private final SettingsModelString packageSettings = createPackageSettings();
	private final SettingsModelString radioSettings = createRadioSettings();
	private final HashMap<String, SettingsModelBoolean> booleanSettings = new HashMap<String, SettingsModelBoolean>();

	private final CrawlerEntriesParser entriesParser = new CrawlerEntriesParser();

	protected CrawlerAdapterNodeModel() {
		super(0, 1);
		initializeSettings();
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		String path = null;
		File file = null;
		if (radioSettings.getStringValue().equals(FILE)) {
			path = fileSettingsXML.getStringValue();
			file = new File(fileSettingsXML.getStringValue());
		} else if (radioSettings.getStringValue().equals(DIRECTORY)) {
			path = fileSettingsDIR.getStringValue();
			file = new File(fileSettingsDIR.getStringValue());
		}
		SourceCrawlerOutput result = null;
		if (file.isDirectory()) {
			result = entriesParser.parseFromExecutableJar(path, booleanSettings
					.get(CREATE_XML).getBooleanValue());
		} else if (file.isFile()) {
			result = entriesParser.parseFromXML(path);
		} else
			throw new IOException(
					"Path does not point at any file or directory");
		optionsFilter(result);
		BufferedDataContainer container = createDataContainer(exec);
		BufferedDataTable out = transformIntoTable(container, result, exec);
		return new BufferedDataTable[] { out };
	}

	private void optionsFilter(SourceCrawlerOutput result) {
		HashMap<String, Boolean> currentSettings = new HashMap<String, Boolean>();
		for (Entry<String, SettingsModelBoolean> entry : booleanSettings
				.entrySet()) {
			currentSettings.put(entry.getKey(), entry.getValue()
					.getBooleanValue());
		}
		CrawlerOptionsParser optionsParser = new CrawlerOptionsParser(
				currentSettings, packageSettings.getStringValue());
		optionsParser.checkRequirements(result);
	}

	private BufferedDataTable transformIntoTable(
			final BufferedDataContainer container,
			final SourceCrawlerOutput classes, final ExecutionContext exec)
			throws CanceledExecutionException {
		List<SourceFile> sourceFiles = classes.getSourceFiles();
		fillTable(container, exec, sourceFiles);
		container.close();
		BufferedDataTable out = container.getTable();
		return out;
	}

	private void fillTable(final BufferedDataContainer container,
			final ExecutionContext exec, final List<SourceFile> sourceFiles)
			throws CanceledExecutionException {
		AtomicInteger counter = new AtomicInteger(0);
		int size = sourceFiles.size();
		for (int i = 0; i < size; i++) {
			progress(exec, size, i);
			List<Clazz> clazzes = entriesParser
					.parseClassesFromFile(sourceFiles.get(i));
			addClassesToDatatable(container, sourceFiles.get(i), clazzes,
					counter);
		}
	}

	private void addClassesToDatatable(final BufferedDataContainer container,
			final SourceFile sourceFile, final List<Clazz> classesInSource,
			AtomicInteger counter) {
		for (Clazz clazz : classesInSource) {
			addRowToTable(container, Long.toString(counter.getAndIncrement()),
					clazz.getName(), clazz.getAccess(), clazz.getType(),
					clazz.isException(), clazz.isInner(), clazz.isTest(),
					clazz.isFinal(), sourceFile.getSourcePackage(),
					sourceFile.getPath());
		}
	}

	private void addRowToTable(final BufferedDataContainer container,
			final String counter, final String name, final String type,
			String access, final boolean exception, final boolean inner,
			final boolean test, boolean finals, final String sourcePackage,
			final String path) {
		container.addRowToTable(createTableRow(counter, name, access, type,
				exception, inner, test, finals, sourcePackage, path));
	}

	private void progress(final ExecutionContext exec, final int size,
			final int i) throws CanceledExecutionException {
		exec.checkCanceled();
		exec.setProgress(i / size);
	}

	private BufferedDataContainer createDataContainer(
			final ExecutionContext exec) {
		DataTableSpec outputSpec = createDataColumnSpec();
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		return container;
	}

	@Override
	protected void reset() {
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		return createTableSpec();
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		fileSettingsXML.saveSettingsTo(settings);
		fileSettingsDIR.saveSettingsTo(settings);
		radioSettings.saveSettingsTo(settings);
		packageSettings.saveSettingsTo(settings);
		for (SettingsModelBoolean settingsModelBoolean : booleanSettings
				.values()) {
			settingsModelBoolean.saveSettingsTo(settings);
		}
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileSettingsXML.loadSettingsFrom(settings);
		fileSettingsDIR.loadSettingsFrom(settings);
		radioSettings.loadSettingsFrom(settings);
		packageSettings.loadSettingsFrom(settings);
		for (SettingsModelBoolean settingsModelBoolean : booleanSettings
				.values()) {
			settingsModelBoolean.loadSettingsFrom(settings);
		}
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileSettingsXML.validateSettings(settings);
		fileSettingsDIR.validateSettings(settings);
		radioSettings.validateSettings(settings);
		packageSettings.validateSettings(settings);
		for (SettingsModelBoolean settingsModelBoolean : booleanSettings
				.values()) {
			settingsModelBoolean.validateSettings(settings);
		}
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

	static SettingsModelString createPackageSettings() {
		return new SettingsModelString(PACKAGE_NAME_CONFIG,
				PACKAGE_DEFAULT_VALUE);
	}

	private void initializeSettings() {
		booleanSettings.put(PUBLIC, new SettingsModelBoolean(PUBLIC_CONFIG,
				true));
		booleanSettings.put(PRIVATE, new SettingsModelBoolean(PRIVATE_CONFIG,
				true));
		booleanSettings.put(PROTECTED, new SettingsModelBoolean(
				PROTECTED_CONFIG, true));
		booleanSettings.put(PACKAGE, new SettingsModelBoolean(PACKAGE_CONFIG,
				true));
		booleanSettings
				.put(CLASS, new SettingsModelBoolean(CLASS_CONFIG, true));
		booleanSettings.put(INTERFACE, new SettingsModelBoolean(
				INTERFACE_CONFIG, true));
		booleanSettings.put(ABSTRACT, new SettingsModelBoolean(ABSTRACT_CONFIG,
				true));
		booleanSettings.put(ENUM, new SettingsModelBoolean(ENUM_CONFIG, true));
		booleanSettings.put(EXCEPTION, new SettingsModelBoolean(
				EXCEPTION_CONFIG, true));
		booleanSettings
				.put(INNER, new SettingsModelBoolean(INNER_CONFIG, true));
		booleanSettings.put(TEST, new SettingsModelBoolean(TEST_CONFIG, true));
		booleanSettings
				.put(FINAL, new SettingsModelBoolean(FINAL_CONFIG, true));
		booleanSettings.put(CREATE_XML, new SettingsModelBoolean(
				CREATE_XML_CONFIG, false));
	}

}
