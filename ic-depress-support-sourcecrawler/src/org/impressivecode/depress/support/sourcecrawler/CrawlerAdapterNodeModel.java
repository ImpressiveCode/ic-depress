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
import java.util.List;
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
 * 
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 * 
 */
public class CrawlerAdapterNodeModel extends NodeModel {
	
    private static final String FILE_DEFAULT_VALUE = "";
    private static final String PACKAGE_DEFAULT_VALUE = "org.";
    
    private static final String FILE_NAME = "depress.sourcecrawler.file";
    private static final String PACKAGE_NAME = "depress.sourcecrawler.package";
    private static final String CREATE_XML = "depress.sourcecrawler.xml";
    private static final String PUBLIC = "depress.sourcecrawler.public";
    private static final String PRIVATE = "depress.sourcecrawler.private";
    private static final String PROTECTED = "depress.sourcecrawler.protected";
    private static final String PACKAGE = "depress.sourcecrawler.packageprivate";
    private static final String CLASS = "depress.sourcecrawler.class";
    private static final String INTERFACE = "depress.sourcecrawler.interface";
    private static final String ABSTRACT = "depress.sourcecrawler.abstract";
    private static final String ENUM = "depress.sourcecrawler.enum";
    private static final String EXCEPTION = "depress.sourcecrawler.exception";
    private static final String INNER = "depress.sourcecrawler.inner";
    private static final String TEST = "depress.sourcecrawler.test";
    private static final String FINAL = "depress.sourcecrawler.final";
	
	private final SettingsModelString fileSettings = createFileSettings();
	private final SettingsModelString packageSettings = createPackageSettings();
	private final SettingsModelBoolean createXML = createXMLSettings();
    private final SettingsModelBoolean booleanPublic = createSettingsModelPublic();
    private final SettingsModelBoolean booleanPrivate = createSettingsModelPrivate();
    private final SettingsModelBoolean booleanProtected = createSettingsModelProtected();
    private final SettingsModelBoolean booleanPackage = createSettingsModelPackage();
    private final SettingsModelBoolean booleanClass = createSettingsModelClass();
    private final SettingsModelBoolean booleanInterface = createSettingsModelInterface();
    private final SettingsModelBoolean booleanAbstract = createSettingsModelAbstract();
    private final SettingsModelBoolean booleanEnum = createSettingsModelEnum();
    private final SettingsModelBoolean booleanException = createSettingsModelException();
    private final SettingsModelBoolean booleanInner = createSettingsModelInner();
    private final SettingsModelBoolean booleanTest = createSettingsModelTest();
    private final SettingsModelBoolean booleanFinal = createSettingsModelFinal();
	
    private final CrawlerEntriesParser entriesParser = new CrawlerEntriesParser();
    
    protected CrawlerAdapterNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {
        String path = fileSettings.getStringValue();
        File file = new File(fileSettings.getStringValue());
        SourceCrawlerOutput result = null;
        if(file.isDirectory()){
       		result = entriesParser.parseFromExecutableJar(path, createXML.getBooleanValue());
        }
        else if(file.isFile()){
        	result = entriesParser.parseFromXML(path);
        } else 
        	throw new IOException("Path does not point at any file or directory");
        optionsFilter(result);
        BufferedDataContainer container = createDataContainer(exec);
        BufferedDataTable out = transformIntoTable(container, result, exec);
        return new BufferedDataTable[] { out };
    }

    private void optionsFilter(SourceCrawlerOutput result) {
        CrawlerOptionsParser optionsParser = new CrawlerOptionsParser(booleanPublic.getBooleanValue(), 
    			booleanPrivate.getBooleanValue(), booleanProtected.getBooleanValue(), booleanPackage.getBooleanValue(), 
    			booleanClass.getBooleanValue(), booleanInterface.getBooleanValue(), booleanAbstract.getBooleanValue(), 
    			booleanEnum.getBooleanValue(), booleanException.getBooleanValue(), booleanInner.getBooleanValue(), 
    			booleanTest.getBooleanValue(), booleanFinal.getBooleanValue(), packageSettings.getStringValue());
        optionsParser.checkRequirements(result);
	}

	private BufferedDataTable transformIntoTable(final BufferedDataContainer container, final SourceCrawlerOutput classes,
            final ExecutionContext exec) throws CanceledExecutionException {
        List<SourceFile> sourceFiles = classes.getSourceFiles();
        fillTable(container, exec, sourceFiles);
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private void fillTable(final BufferedDataContainer container, final ExecutionContext exec,
            final List<SourceFile> sourceFiles) throws CanceledExecutionException {
    	AtomicInteger counter = new AtomicInteger(0);
    	int size = sourceFiles.size();
    	for(int i = 0; i < size; i++){
            progress(exec, size, i);
        	List<Clazz> clazzes = entriesParser.parseClassesFromFile(sourceFiles.get(i));
            addClassesToDatatable(container, sourceFiles.get(i), clazzes, counter);
        }
    }

    private void addClassesToDatatable(final BufferedDataContainer container, final SourceFile sourceFile,
            final List<Clazz> classesInSource, AtomicInteger counter) {
        for (Clazz clazz : classesInSource) {
            addRowToTable(container, Long.toString(counter.getAndIncrement()), clazz.getName(),
            		clazz.getAccess(), clazz.getType(), clazz.isException(), clazz.isInner(),
            		clazz.isTest(), clazz.isFinal(), sourceFile.getSourcePackage(), sourceFile.getPath());
        }
    }

    private void addRowToTable(final BufferedDataContainer container, final String counter, final String name,
    		final String type, String access, final boolean exception, final boolean inner, final boolean test,
    		boolean finals, final String sourcePackage, final String path) {
        container.addRowToTable(createTableRow(counter, name, access, type, exception, inner, test, finals, sourcePackage, path));
    }

    private void progress(final ExecutionContext exec, final int size, final int i) throws CanceledExecutionException {
        exec.checkCanceled();
        exec.setProgress(i / size);
    }

    private BufferedDataContainer createDataContainer(final ExecutionContext exec) {
        DataTableSpec outputSpec = createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);
        return container;
    }

    @Override
    protected void reset() {
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	fileSettings.saveSettingsTo(settings);
    	packageSettings.saveSettingsTo(settings);
    	createXML.saveSettingsTo(settings);
        booleanPublic.saveSettingsTo(settings);
        booleanPrivate.saveSettingsTo(settings);
        booleanProtected.saveSettingsTo(settings);
        booleanPackage.saveSettingsTo(settings);
        booleanClass.saveSettingsTo(settings);
        booleanInterface.saveSettingsTo(settings);
        booleanAbstract.saveSettingsTo(settings);
        booleanEnum.saveSettingsTo(settings);
        booleanException.saveSettingsTo(settings);
        booleanInner.saveSettingsTo(settings);
        booleanTest.saveSettingsTo(settings);
        booleanFinal.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
    	fileSettings.loadSettingsFrom(settings);
    	packageSettings.loadSettingsFrom(settings);
    	createXML.loadSettingsFrom(settings);
        booleanPublic.loadSettingsFrom(settings);
        booleanPrivate.loadSettingsFrom(settings);
        booleanProtected.loadSettingsFrom(settings);
        booleanPackage.loadSettingsFrom(settings);
        booleanClass.loadSettingsFrom(settings);
        booleanInterface.loadSettingsFrom(settings);
        booleanAbstract.loadSettingsFrom(settings);
        booleanEnum.loadSettingsFrom(settings);
        booleanException.loadSettingsFrom(settings);
        booleanInner.loadSettingsFrom(settings);
        booleanTest.loadSettingsFrom(settings);
        booleanFinal.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    	fileSettings.validateSettings(settings);
    	packageSettings.validateSettings(settings);
    	createXML.validateSettings(settings);
        booleanPublic.validateSettings(settings);
        booleanPrivate.validateSettings(settings);
        booleanProtected.validateSettings(settings);
        booleanPackage.validateSettings(settings);
        booleanClass.validateSettings(settings);
        booleanInterface.validateSettings(settings);
        booleanAbstract.validateSettings(settings);
        booleanEnum.validateSettings(settings);
        booleanException.validateSettings(settings);
        booleanInner.validateSettings(settings);
        booleanTest.validateSettings(settings);
        booleanFinal.validateSettings(settings);
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
        // NOOP
    }
    
    static SettingsModelString createFileSettings() {
        return new SettingsModelString(FILE_NAME, FILE_DEFAULT_VALUE);
    }
	static SettingsModelString createPackageSettings() {
		return new SettingsModelString(PACKAGE_NAME, PACKAGE_DEFAULT_VALUE);
	}
	static SettingsModelBoolean createXMLSettings() {
		return new SettingsModelBoolean(CREATE_XML, false);
	}
    static SettingsModelBoolean createSettingsModelPublic() {
        return new SettingsModelBoolean(PUBLIC, true);
    }
    static SettingsModelBoolean createSettingsModelPrivate() {
        return new SettingsModelBoolean(PRIVATE, true);
    }
    static SettingsModelBoolean createSettingsModelProtected() {
        return new SettingsModelBoolean(PROTECTED, true);
    }
    static SettingsModelBoolean createSettingsModelPackage() {
        return new SettingsModelBoolean(PACKAGE, true);
    }
    static SettingsModelBoolean createSettingsModelClass() {
        return new SettingsModelBoolean(CLASS, true);
    }
    static SettingsModelBoolean createSettingsModelInterface() {
        return new SettingsModelBoolean(INTERFACE, true);
    }
    static SettingsModelBoolean createSettingsModelAbstract() {
        return new SettingsModelBoolean(ABSTRACT, true);
    }
    static SettingsModelBoolean createSettingsModelEnum() {
        return new SettingsModelBoolean(ENUM, true);
    }
    static SettingsModelBoolean createSettingsModelException() {
        return new SettingsModelBoolean(EXCEPTION, true);
    }
    static SettingsModelBoolean createSettingsModelInner() {
        return new SettingsModelBoolean(INNER, true);
    }
    static SettingsModelBoolean createSettingsModelTest() {
        return new SettingsModelBoolean(TEST, true);
    }
    static SettingsModelBoolean createSettingsModelFinal() {
        return new SettingsModelBoolean(FINAL, true);
    }
   
}
