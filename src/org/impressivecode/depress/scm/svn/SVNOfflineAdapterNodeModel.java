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
package org.impressivecode.depress.scm.svn;

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.scm.SCMParserOptions.options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections; 

import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.SCMAdapterTransformer;
import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.SCMParserOptions;
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
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 
 * @author Marek Majchrzak, ImpressiveCode
 * @author Krystian Dabrowski, Capgemini Poland
 * @author Zuzanna Pacholczyk, Capgemini Poland
 * 
 */
public class SVNOfflineAdapterNodeModel extends NodeModel {

    private static final NodeLogger LOGGER = NodeLogger.getLogger(SVNOfflineAdapterNodeModel.class);

    static final String CFG_FILENAME = "filename";
    static final String CFG_PACKAGENAME = "package";
    static final String CFG_EXTENSION = "extension";
    
    static final String FILENAME_DEFAULT = "";
    static final String PACKAGENAME_DEFAULT = "";
    static final String EXTENSION_DEFAULT = ".java";


    private final SettingsModelString fileName = new SettingsModelString(SVNOfflineAdapterNodeModel.CFG_FILENAME,
            SVNOfflineAdapterNodeModel.FILENAME_DEFAULT);
    
    private final SettingsModelOptionalString packageName = new SettingsModelOptionalString(
            SVNOfflineAdapterNodeModel.CFG_PACKAGENAME, SVNOfflineAdapterNodeModel.PACKAGENAME_DEFAULT, true);
    
    public final SettingsModelString extensions = new SettingsModelString(
    		SVNOfflineAdapterNodeModel.CFG_EXTENSION, SVNOfflineAdapterNodeModel.EXTENSION_DEFAULT);
    
    private ArrayList<String> userExtensions;

	protected SVNOfflineAdapterNodeModel() {
        super(0, 1);
      
    
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        try {
            LOGGER.info("Reading logs from file " + this.fileName.getStringValue());
            userExtensions = new ArrayList<String>(); 
            Collections.addAll( userExtensions, getExtensions());
            String packageNameToFilter = Strings.emptyToNull(packageName.getStringValue());
            SCMParserOptions parserOptions = options(packageNameToFilter, userExtensions); 
            
            SVNExtensionParser parser = new SVNExtensionParser(parserOptions);
   
            List<SCMDataType> commits = parser.parseEntries(this.fileName.getStringValue());
            
            LOGGER.info("Reading logs finished");
            BufferedDataTable out = transform(commits, exec);
            LOGGER.info("Transforming logs finished.");
            return new BufferedDataTable[] { out };
        } catch (Exception ex) {
            LOGGER.error("Unable to parse SVN entries", ex);
            throw ex;
        }

    }

	private BufferedDataTable transform(final List<SCMDataType> commits, final ExecutionContext exec)
            throws CanceledExecutionException {
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(commits, exec);
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return SCMAdapterTableFactory.createTableSpec();
    }

    
      
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        fileName.saveSettingsTo(settings);
        packageName.saveSettingsTo(settings);
        extensions.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileName.loadSettingsFrom(settings);
        packageName.loadSettingsFrom(settings);
        extensions.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        fileName.validateSettings(settings);
        packageName.validateSettings(settings);
        extensions.validateSettings(settings);
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
    
    // parsing user's extensions into a list
	private String[] getExtensions() {
		String ext = extensions.getStringValue();
		String[] ext_ = ext.split("\\s*,\\s*");
		for(String word : ext_){
			if(word.equals("*")){
				ext_[0] = "*";
				break;
			}			
		}
		return ext_;
	}
    
}
