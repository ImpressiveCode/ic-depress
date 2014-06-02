package org.impressivecode.depress.metric.astcompare.ast;

import static org.impressivecode.depress.metric.astcompare.utils.Utils.saveStreamAsFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.team.core.history.IFileRevision;
import org.impressivecode.depress.metric.astcompare.db.DbHandler;
import org.impressivecode.depress.metric.astcompare.svn.SvnHandler;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

/**
 * @author Piotr Mitka
 */
public class AstController {

    private final ExecutionContext exec;
    private FileDistiller distiller;
    private DbHandler db;
    private File previous;
    private File actual;
    private SvnHandler svnHandler;

    public AstController(final ExecutionContext exec, DbHandler db, SvnHandler svnHandler) {
        this.distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
        this.db = db;
        this.exec = exec;
        this.svnHandler = svnHandler;
    }

    public void collectDataAndSaveInDb(IPackageFragment[] packages, String selectedProjectName, long revisionDateMin,
            long revisionDateMax) throws CanceledExecutionException, CoreException, IOException, SQLException {
        double progressIndex = 1.0d;
        previous = File.createTempFile("astFileA", ".java");
        actual = File.createTempFile("astFileB", ".java");

        for (IPackageFragment mypackage : packages) {
            checkIfCancelledAndSetProgress((progressIndex++ / packages.length) * 0.8d);

            if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {

                for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
                    checkIfCancelledAndSetProgress(null);

                    IFileRevision[] revisions = svnHandler.getProperFileRevisions(unit.getResource());

                    if (revisions != null && revisions.length > 1) {

                        InputStream previousStream = null;

                        for (IFileRevision revision : revisions) {
                            checkIfCancelledAndSetProgress(null);

                            if (previousStream == null) {
                                previousStream = revision.getStorage(null).getContents();

                                saveStreamAsFile(previousStream, previous);

                                continue;
                            } else {
                                saveStreamAsFile(revision.getStorage(null).getContents(), actual);

                                compareAstAndSaveToDb(selectedProjectName, revisionDateMin, revisionDateMax, revision);

                                swapActualAndPreviousFile(actual, previous);
                            }

                        }
                        previous.delete();
                        actual.delete();
                    }
                }
            }
        }
    }

    private void swapActualAndPreviousFile(File actual, File previous) {
        File tmp = previous;
        previous = actual;
        actual = tmp;
        actual.delete();
    }

    private void compareAstAndSaveToDb(String selectedProjectName, long revisionDateMin, long revisionDateMax,
            IFileRevision revision) throws CanceledExecutionException, SQLException {
        distiller.extractClassifiedSourceCodeChanges(previous, actual);

        if (distiller.getSourceCodeChanges() != null) {
            for (SourceCodeChange scc : distiller.getSourceCodeChanges()) {
                checkIfCancelledAndSetProgress(null);
                SingleChangeInfo info = SourceCodeChangeExtractor.extractChanges(revision, scc);

                // only method level
                if (info.getMethodName() != null && info.hasDataCorrect()) {
                    db.insertRevisionData(info, selectedProjectName, revisionDateMin, revisionDateMax);
                }
            }
        }
    }

    private void checkIfCancelledAndSetProgress(Double progress) throws CanceledExecutionException {
        if (exec != null) {
            exec.checkCanceled();

            // no progress change
            if (progress != null) {
                exec.setProgress(progress);
            }
        }
    }
}
