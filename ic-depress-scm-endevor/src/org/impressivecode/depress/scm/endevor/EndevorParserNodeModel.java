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
package org.impressivecode.depress.scm.endevor;

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.SCMAdapterTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMOperation;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

/**
 * 
 * @author Alicja Bodys, Wroc³aw University of Technology
 * @author Mateusz Leonowicz, Wroc³aw University of Technology
 * @author Piotr Sas, Wroc³aw University of Technology
 * @author Maciej Sznurowski, Wroc³aw University of Technology
 * 
 */
public class EndevorParserNodeModel extends NodeModel {
    
    private static final NodeLogger logger = NodeLogger.getLogger(EndevorParserNodeModel.class);
        
	public static final String CFG_SOURCEFILE_NAME = "KeyFilename";
	
	public static final String SOURCEFILE_NAME_DEFAULT = "";

    private static final SettingsModelString smSelectedFilename = new SettingsModelString(CFG_SOURCEFILE_NAME, SOURCEFILE_NAME_DEFAULT);
    

    protected EndevorParserNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws Exception {

        logger.info(String.format("Endevor log processing started from file %s...", smSelectedFilename.getStringValue()));
        System.out.println(String.format("Endevor log processing started from file %s...", smSelectedFilename.getStringValue()));
        
        //TODO parser - parsowanie razem z wczytywaniem ploiku. GUAVA tylko do testow
        String fileContent = loadTextFile(new File(smSelectedFilename.getStringValue()));
        List<SCMDataType> commits = new LinkedList<SCMDataType>();
        
        //TODO delete test data
        SCMDataType testData = new SCMDataType();
        testData.setAuthor("Test author");
        testData.setCommitDate(new Date(System.currentTimeMillis()));
        testData.setResourceName("Test resource");
        testData.setOperation(SCMOperation.OTHER);
        testData.setPath(smSelectedFilename.getStringValue());
        testData.setCommitID("testCommID");
        testData.setExtension("test ext");
        testData.setMessage("Test output");
        commits.add(testData);
        
        BufferedDataTable out = transform(commits, exec);

        return new BufferedDataTable[]{ out };
    }
    
	private String loadTextFile(File sourceFile) {
		try {
			return Files.toString(sourceFile, Charset.forName("UTF-8"));
		} catch (IOException e) {
			return null;
		}
	}

	private BufferedDataTable transform(final List<SCMDataType> commits, final ExecutionContext exec) throws CanceledExecutionException {
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(commits, exec);
    }

    @Override
    protected void reset() {
    	//nothing
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return SCMAdapterTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        smSelectedFilename.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        smSelectedFilename.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    	smSelectedFilename.validateSettings(settings);
    }
    
    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
        //nothing
    }
    
    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException, CanceledExecutionException {
    	//nothing
    }

}