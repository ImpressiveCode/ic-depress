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

import static org.impressivecode.depress.scm.git.GitTableFactory.createTableRow;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
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


/**
 * This is the model implementation of Git.
 * 
 *
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitNodeModel extends NodeModel {

    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(GitNodeModel.class);

    static final String GIT_FILENAME = "depress.scm.git.filename";
    static final String GIT_FILENAME_DEFAULT = "";
    static final String GIT_REGEXP = "depress.scm.git.regexp";
    static final String GIT_REGEXP_DEFAULT = "";
    static final String GIT_PACKAGENAME = "depress.scm.git.package";
    static final String GIT_PACKAGENAME_DEFAULT = "org";

    static final Boolean GIT_PACKAGENAME_ACTIVE_STATE = false;

    public GitLogParser parser;

    // example value: the models count variable filled from the dialog
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString gitFileName =  new SettingsModelString(GitNodeModel.GIT_FILENAME,
            GitNodeModel.GIT_FILENAME_DEFAULT);
    private final SettingsModelString gitRegExp = new SettingsModelString(GitNodeModel.GIT_REGEXP,
            GitNodeModel.GIT_REGEXP_DEFAULT);
    private final SettingsModelOptionalString gitPackageName = new SettingsModelOptionalString(GitNodeModel.GIT_PACKAGENAME,
            GitNodeModel.GIT_PACKAGENAME_DEFAULT,
            GitNodeModel.GIT_PACKAGENAME_ACTIVE_STATE);

    /**
     * Constructor for the node model.
     */
    protected GitNodeModel() {

        super(0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {

        logger.info("Reading logs from file "+gitFileName);
        DataTableSpec outputSpec = GitTableFactory.createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);

        logger.info("Creating Output table with data from git file...");
        //trzeba przygotować tabelę wyjściową
        this.parser = new GitLogParser(this.gitFileName.getStringValue());
        List<GitCommit> commits = this.parser.parse();

        BufferedDataTable out = transform(container, commits, exec);
        logger.info("Reading git logs finished.");

        return new BufferedDataTable[]{out};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO Code executed on reset.
        // Models build during execute are cleared here.
        // Also data handled in load/saveInternals will be erased here.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {

        // TODO: check if user settings are available, fit to the incoming
        // table structure, and the incoming types are feasible for the node
        // to execute. If the node can execute in its current state return
        // the spec of its output data table(s) (if you can, otherwise an array
        // with null elements), or throw an exception with a useful user message

        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        gitFileName.saveSettingsTo(settings);
        gitRegExp.saveSettingsTo(settings);
        gitPackageName.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        gitFileName.loadSettingsFrom(settings);
        gitRegExp.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {

        gitFileName.validateSettings(settings);
        gitRegExp.validateSettings(settings);
        gitPackageName.validateSettings(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

        // TODO load internal data.
        // Everything handed to output ports is loaded automatically (data
        // returned by the execute method, models loaded in loadModelContent,
        // and user settings set through loadSettingsFrom - is all taken care
        // of). Load here only the other internals that need to be restored
        // (e.g. data used by the views).

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {

        // TODO save internal models.
        // Everything written to output ports is saved automatically (data
        // returned by the execute method, models saved in the saveModelContent,
        // and user settings saved through saveSettingsTo - is all taken care
        // of). Save here only the other internals that need to be preserved
        // (e.g. data used by the views).

    }

    private BufferedDataTable transform(final BufferedDataContainer container, final List<GitCommit> commits,
            final ExecutionContext exec) throws CanceledExecutionException {

        int size = commits.size();

        try {
            for (int i = 0; i < size; i++) {
                progress(exec, size, i);

                GitCommit commit = commits.get(i);
                if (logger.isDebugEnabled()) {
                    logger.debug("Transforming commit: " + commit.getId());
                }

                for (GitCommitFile file : commit.files){
                    if (this.isClassFile(file.getPath())){
                        Set<String> marker = this.getMarkers(commit.getMessage());
                        String author = commit.getAuthor();
                        String operation = file.getOperation().toString();
                        String message = commit.getMessage();
                        String path = file.getPath();
                        String className = this.getClassNameFromPath(path);
                        String commitDate = this.parseDate(commit.getDate());
                        String uid = this.calculateMd5(commit.getId(), file.getPath());
                        String commit_id = commit.getId();
                        int churn = 0;
                        if (file.isTextFile()) {
                            churn = file.getChurn();
                        }

                        addRowToTable(container, className, marker, author, operation, message, path, commitDate, uid, commit_id, churn);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
        container.close();
        BufferedDataTable out = container.getTable();
        return out;
    }

    private void progress(final ExecutionContext exec, final int size, final int i) throws CanceledExecutionException {
        exec.checkCanceled();
        exec.setProgress(i / size);
    }

    private void addRowToTable(final BufferedDataContainer container,
            final String className, final Set<String> marker, final String author, final String operation,
            final String message, final String path, final String commitDate, final String uid, final String commitID, final int churn) {
        container.addRowToTable(createTableRow(className, marker, author, operation, message, path, commitDate, uid, commitID, churn));
    }

    private String parseDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }

    //TODO: ta metoda powinna raczej być przeniesiona do GitCommitFile, ale to jeszcze do omówienia z Tomkiem:
    private String getClassNameFromPath(String path){
        String class_name;
        String[] package_path_to_class;
        if (this.gitPackageName.isActive()){
            package_path_to_class = path.split(this.gitPackageName.getStringValue());
            if (package_path_to_class.length >= 2) {
                class_name = this.gitPackageName.getStringValue()+package_path_to_class[1];
                class_name = class_name.replaceAll("/", ".");
                class_name = class_name.substring(0, class_name.lastIndexOf("."));
            } else {
                //if there is no gitPackageName value in path to file
                //than this is probably not class file but some other file and than class name is empty:
                class_name = "";
            }
        } else {
            class_name = path;
        }
        return class_name;
    }

    private String calculateMd5(String value1, String value2) throws NoSuchAlgorithmException{
        String input = value1 + value2;
        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private boolean isClassFile(String path){
        String file_extension = path.substring(path.lastIndexOf("."));
        return file_extension.equals(".java");
    }

    private Set<String> getMarkers(String message) {
        Pattern markersRegex = Pattern.compile(this.gitRegExp.getStringValue());
        Set<String> markers = new HashSet<String>();
        //if markers regex is empty than we don't search markers:
        if (!markersRegex.pattern().isEmpty()){
            Matcher markersMatcher = markersRegex.matcher(message);
            String currentCommitMarker;
            if (markersMatcher.find()){
                currentCommitMarker = message.substring(markersMatcher.end()).split(" ", 2)[0];
                if (!currentCommitMarker.isEmpty()){
                    markers.add(currentCommitMarker);
                }
            }
        }
        return markers;
    }

}

