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
package org.impressivecode.depress.scm.git;

import static org.impressivecode.depress.scm.SCMAdapterTableFactory.createDataColumnSpec;
import static org.impressivecode.depress.scm.git.GitParserOptions.options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
import org.impressivecode.depress.scm.SCMAdapterTransformer;
import org.impressivecode.depress.scm.SCMDataType;
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
import com.google.common.collect.Lists;

/**
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class GitOfflineAdapterNodeModel extends NodeModel {

    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger(GitOfflineAdapterNodeModel.class);
    
    static final String GIT_FILENAME = "depress.scm.git.filename";
    static final String GIT_FILENAME_DEFAULT = "";
    static final String GIT_PACKAGENAME = "depress.scm.git.package";
    static final String GIT_PACKAGENAME_DEFAULT = "org.";
    static final String EXTENSION_STR = "depress.scm.svn.string";
    static final String EXTENSION_DEFAULT = ".java";
    
    static final Boolean GIT_PACKAGENAME_ACTIVE_STATE = false;

    // example value: the models count variable filled from the dialog
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString gitFileName = new SettingsModelString(GitOfflineAdapterNodeModel.GIT_FILENAME,
            GitOfflineAdapterNodeModel.GIT_FILENAME_DEFAULT);
    public static final SettingsModelOptionalString gitPackageName = new SettingsModelOptionalString(
            GitOfflineAdapterNodeModel.GIT_PACKAGENAME, GitOfflineAdapterNodeModel.GIT_PACKAGENAME_DEFAULT, true);
    
    public static final SettingsModelString extensions = new SettingsModelString(
    		GitOfflineAdapterNodeModel.EXTENSION_STR, GitOfflineAdapterNodeModel.EXTENSION_DEFAULT);
    
    protected GitOfflineAdapterNodeModel() {
        super(0, 1);
    }
    
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
    	
        try {
            logger.info("Reading git logs from file " + this.gitFileName.getStringValue());
            ArrayList<String> userExtensions = getExtensions(); 
            String packageNameToFilter = Strings.emptyToNull(gitPackageName.getStringValue());
            GitParserOptions parserOptions = options(packageNameToFilter, userExtensions, ""); //empty branch name
            GitOfflineLogParser parser = new GitOfflineLogParser(parserOptions);
            List<GitCommit> commits = parser.parseEntries(this.gitFileName.getStringValue());
            logger.info("Reading git logs finished");
            BufferedDataTable out = transform(commits, exec);
            logger.info("Transforming git logs finished.");
            return new BufferedDataTable[] { out };
        } catch (Exception ex) {
        	logger.error("Unable to parse git entries", ex);
            throw ex;
        }
    }

	@Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return GitAdapterTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        gitFileName.saveSettingsTo(settings);
        gitPackageName.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        gitFileName.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        gitFileName.validateSettings(settings);
        gitPackageName.validateSettings(settings);
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

    private BufferedDataTable transform(final List<GitCommit> commits, final ExecutionContext exec)
            throws CanceledExecutionException {
        List<SCMDataType> data = Lists.newLinkedList();
        for (GitCommit commit : commits) {
            for (GitCommitFile file : commit.getFiles()) {
                progress(exec);
                data.add(scm(commit, file));
            }
        }
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(data, exec);
    }

    private SCMDataType scm(final GitCommit commit, final GitCommitFile file) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor(commit.getAuthor());
        scm.setCommitDate(commit.getDate());
        scm.setCommitID(commit.getId());
        scm.setMessage(commit.getMessage());
        scm.setOperation(file.getOperation());
        scm.setPath(file.getPath());
        scm.setResourceName(file.getJavaClass());
        scm.setExtension(file.getExtension());
        return scm;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }
    
    // parsing user's extensions into a list
  	private ArrayList<String> getExtensions() {
  		String ext = extensions.getStringValue();
  		String[] ext_ = ext.split("\\s*,\\s*");
  		ArrayList<String> arr = new ArrayList<String>();
  		for(String word : ext_){
  			if(word.equals("*")){
  				arr = new ArrayList<String>();
  				arr.add("*");
  				break;
  			}
  			arr.add(word);
  		}
  		return arr;
  	}
}
