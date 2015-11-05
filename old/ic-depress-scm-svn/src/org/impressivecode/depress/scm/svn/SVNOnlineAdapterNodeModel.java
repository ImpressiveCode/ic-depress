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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.impressivecode.depress.common.OutputTransformer;
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
import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * 
 * @author IcDepress
 * @author Zuzanna Pacholczyk, Capgemini Poland
 */
public class SVNOnlineAdapterNodeModel extends NodeModel {

    private static final NodeLogger logger = NodeLogger.getLogger(SVNOnlineAdapterNodeModel.class);

    public static String SVN_REPOSITORY_ADDRESS = "remote address";
    public static String SVN_LOGIN = "login";
    public static String SVN_PASSWORD = "password";
    public final static String SVN_EXTENSION = "extension";
    public static String SVN_PACKAGENAME = "package";

    public static String SVN_PACKAGENAME_DEFAULT = "";
    public static String EXTENSION_DEFAULT = ".java";
    public static String SVN_REPOSITORY_DEFAULT = "";
    public static String SVN_LOGIN_DEFAULT = "";
    public static String SVN_PASSWORD_DEFAULT = "";

    private final SettingsModelString svnRepositoryAddress = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_REPOSITORY_ADDRESS, SVNOnlineAdapterNodeModel.SVN_REPOSITORY_DEFAULT);
    
    private final SettingsModelString svnLogin = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_LOGIN, SVNOnlineAdapterNodeModel.SVN_LOGIN_DEFAULT);
    
    private final SettingsModelString svnPassword = new SettingsModelString(
            SVNOnlineAdapterNodeModel.SVN_PASSWORD, SVNOnlineAdapterNodeModel.SVN_PASSWORD_DEFAULT);
    
    private final SettingsModelOptionalString svnPackageName = new SettingsModelOptionalString(
            SVNOnlineAdapterNodeModel.SVN_PACKAGENAME, SVNOnlineAdapterNodeModel.SVN_PACKAGENAME_DEFAULT, true);
    
    public final SettingsModelString extensions = new SettingsModelString(
    		SVNOnlineAdapterNodeModel.SVN_EXTENSION, SVNOnlineAdapterNodeModel.EXTENSION_DEFAULT);
    
    private ArrayList<String> userExtensions;

    protected SVNOnlineAdapterNodeModel() {
        super(0, 1);
    }

    
    /* TODO: following methods should be adjusted to offline model methods */
    
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {
        try {
	    	userExtensions = new ArrayList<String>(); 
	        Collections.addAll( userExtensions, getExtensions());
	        String svnPath = getSvnPath(this.svnRepositoryAddress.getStringValue());
	        String svnLog = this.svnLogin.getStringValue();
	        String svnPass = this.svnPassword.getStringValue();
	
	        logger.info("Reading logs from repository " + svnPath);
	        SVNOnlineLogParser parser = new SVNOnlineLogParser();
	        SCMParserOptions parserOptions = options(svnPackageName.getStringValue(), userExtensions);
	        List<SVNCommit> commits = parser.parseEntries(svnPath, svnLog, svnPass, parserOptions);

	        logger.info("Reading git logs finished.");
	        BufferedDataTable out = transform(commits, exec);	
	        logger.info("Transforming logs finished.");
	        return new BufferedDataTable[] { out };
	    } catch (Exception ex) {
	    	logger.error("Unable to parse SVN entries", ex);
	        throw ex;
        }
    }
    
    private BufferedDataTable transform(final List<SVNCommit> commits, final ExecutionContext exec)
            throws CanceledExecutionException {
        List<SCMDataType> data = Lists.newLinkedList();
        for (SVNCommit commit : commits) {
            for (SVNCommitFile file : commit.getFiles()) {
                progress(exec);
                data.add(scm(commit, file));
            }
        }
        OutputTransformer<SCMDataType> transformer = new SCMAdapterTransformer(createDataColumnSpec());
        return transformer.transform(data, exec);
    }
	
    private SCMDataType scm(final SVNCommit commit, final SVNCommitFile file) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor(commit.getAuthor());
        scm.setCommitDate(commit.getDate());
        scm.setCommitID(commit.getId());
        scm.setMessage(commit.getMessage());
        scm.setOperation(file.getOperation());
        scm.setPath(file.getPath());
        scm.setExtension(Files.getFileExtension(file.getPath()));
        scm.setResourceName(file.getResourceName());
        return scm;
    }

    private void progress(final ExecutionContext exec) throws CanceledExecutionException {
        exec.checkCanceled();
    }

    @Override
    protected void reset() {
    	//NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        Preconditions.checkArgument(inSpecs.length == 0);
        return SVNAdapterTableFactory.createTableSpec();
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
        svnRepositoryAddress.saveSettingsTo(settings);
        svnLogin.saveSettingsTo(settings);
        svnPassword.saveSettingsTo(settings);
        svnPackageName.saveSettingsTo(settings);
        extensions.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        svnRepositoryAddress.loadSettingsFrom(settings);
        svnLogin.loadSettingsFrom(settings);
        svnPassword.loadSettingsFrom(settings);
        svnPackageName.loadSettingsFrom(settings);
        extensions.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        svnRepositoryAddress.validateSettings(settings);
        svnLogin.validateSettings(settings);
        svnPassword.validateSettings(settings);
        svnPackageName.validateSettings(settings);
        extensions.validateSettings(settings);
        
    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
    	//NOOP
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
    	//NOOP
    }

    private String getSvnPath(final String repositoryPath) {
        return URI.create(repositoryPath).toString();
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
