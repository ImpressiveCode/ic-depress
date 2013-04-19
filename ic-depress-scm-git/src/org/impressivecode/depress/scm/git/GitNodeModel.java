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

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.impressivecode.depress.scm.SCMAdapterTableFactory;
import org.impressivecode.depress.scm.SCMDataType;
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
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Marek Majchrzak, ImpressiveCode
 */
public class GitNodeModel extends NodeModel {

    // the logger instance
    private static final NodeLogger logger = NodeLogger.getLogger(GitNodeModel.class);

    static final String GIT_FILENAME = "depress.scm.git.filename";
    static final String GIT_FILENAME_DEFAULT = "";
    static final String GIT_REGEXP = "depress.scm.git.regexp";
    static final String GIT_REGEXP_DEFAULT = "";
    static final String GIT_PACKAGENAME = "depress.scm.git.package";
    static final String GIT_PACKAGENAME_DEFAULT = "org";

    static final Boolean GIT_PACKAGENAME_ACTIVE_STATE = false;

    // example value: the models count variable filled from the dialog
    // and used in the models execution method. The default components of the
    // dialog work with "SettingsModels".
    private final SettingsModelString gitFileName = new SettingsModelString(GitNodeModel.GIT_FILENAME,
            GitNodeModel.GIT_FILENAME_DEFAULT);
    private final SettingsModelString gitRegExp = new SettingsModelString(GitNodeModel.GIT_REGEXP,
            GitNodeModel.GIT_REGEXP_DEFAULT);
    private final SettingsModelOptionalString gitPackageName = new SettingsModelOptionalString(
            GitNodeModel.GIT_PACKAGENAME, GitNodeModel.GIT_PACKAGENAME_DEFAULT,
            GitNodeModel.GIT_PACKAGENAME_ACTIVE_STATE);

    protected GitNodeModel() {
        super(0, 1);
    }

    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
            throws Exception {

        logger.info("Reading logs from file " + this.gitFileName.getStringValue());
        DataTableSpec outputSpec = SCMAdapterTableFactory.createDataColumnSpec();
        BufferedDataContainer container = exec.createDataContainer(outputSpec);

        logger.info("Creating Output table with data from git file...");

        GitLogParser parser = new GitLogParser();
        List<GitCommit> commits = parser.parse(this.gitFileName.getStringValue());

        BufferedDataTable out = transform(container, commits, exec);
        logger.info("Reading git logs finished.");

        return new BufferedDataTable[] { out };
    }

    @Override
    protected void reset() {
        // NOOP
    }

    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
        return new DataTableSpec[] { null };
    }

    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {

        gitFileName.saveSettingsTo(settings);
        gitRegExp.saveSettingsTo(settings);
        gitPackageName.saveSettingsTo(settings);
    }

    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {

        gitFileName.loadSettingsFrom(settings);
        gitRegExp.loadSettingsFrom(settings);
        gitPackageName.loadSettingsFrom(settings);
    }

    @Override
    protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

        gitFileName.validateSettings(settings);
        gitRegExp.validateSettings(settings);
        gitPackageName.validateSettings(settings);

    }

    @Override
    protected void loadInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
    }

    @Override
    protected void saveInternals(final File internDir, final ExecutionMonitor exec) throws IOException,
    CanceledExecutionException {
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

                for (GitCommitFile file : commit.files) {
                    if (this.isClassFile(file.getPath())) {
                        Set<String> marker = this.getMarkers(commit.getMessage());
                        String author = commit.getAuthor();
                        String operation = file.getOperation().toString();
                        String message = commit.getMessage();
                        String path = file.getPath();
                        String className = this.getClassNameFromPath(path);
                        String uid = this.calculateMd5(commit.getId(), file.getPath());
                        String commit_id = commit.getId();

                        addRowToTable(container, className, marker, author, operation, message, path, commit.getDate(), uid,
                                commit_id);
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

    private void addRowToTable(final BufferedDataContainer container, final String className, final Set<String> marker,
            final String author, final String operation, final String message, final String path,
            final Date commitDate, final String uid, final String commitID) {
        SCMDataType scm = scm(className, marker, author, operation, message, path, commitDate, uid,
                commitID);
        container.addRowToTable(SCMAdapterTableFactory.createTableRow(uid, scm));
    }

    private SCMDataType scm(final String className, final Set<String> marker, final String author, final String operation, final String message,
            final String path, final Date commitDate, final String uid, final String commitID) {
        SCMDataType scm = new SCMDataType();
        scm.setAuthor(author);
        scm.setCommitDate(commitDate);
        scm.setCommitID(commitID);
        scm.setMarkers(marker);
        scm.setMessage(message);
        scm.setOperation(operation);
        scm.setPath(path);
        scm.setResourceName(className);
        return scm;
    }

    private String getClassNameFromPath(final String path) {
        String class_name;
        String[] package_path_to_class;
        if (this.gitPackageName.isActive()) {
            package_path_to_class = path.split(this.gitPackageName.getStringValue());
            if (package_path_to_class.length >= 2) {
                class_name = this.gitPackageName.getStringValue() + package_path_to_class[1];
                class_name = class_name.replaceAll("/", ".");
                class_name = class_name.substring(0, class_name.lastIndexOf("."));
            } else {
                // if there is no gitPackageName value in path to file
                // than this is probably not class file but some other file and
                // than class name is empty:
                class_name = "";
            }
        } else {
            class_name = path;
        }
        return class_name;
    }

    private String calculateMd5(final String value1, final String value2) throws NoSuchAlgorithmException {
        String input = value1 + value2;
        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private boolean isClassFile(final String path) {
        String file_extension = path.substring(path.lastIndexOf("."));
        return file_extension.equals(".java");
    }

    private Set<String> getMarkers(final String message) {
        Pattern markersRegex = Pattern.compile(this.gitRegExp.getStringValue());
        Set<String> markers = new HashSet<String>();
        // if markers regex is empty than we don't search markers:
        if (!markersRegex.pattern().isEmpty()) {
            Matcher markersMatcher = markersRegex.matcher(message);
            String currentCommitMarker;
            if (markersMatcher.find()) {
                currentCommitMarker = message.substring(markersMatcher.end()).split(" ", 2)[0];
                if (!currentCommitMarker.isEmpty()) {
                    markers.add(currentCommitMarker);
                }
            }
        }
        return markers;
    }
}
