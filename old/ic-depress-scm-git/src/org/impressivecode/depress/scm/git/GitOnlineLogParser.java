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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.impressivecode.depress.scm.SCMExtensionsParser;

/**
 * 
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 */
public class GitOnlineLogParser {


    public List<GitCommit> parseEntries(final String path, final GitParserOptions gitParserOptions) throws IOException,
    ParseException, NoHeadException, GitAPIException {
        checkArgument(!isNullOrEmpty(path), "Path has to be set.");

        List<GitCommit> commitsList = processRepo(path, gitParserOptions);

        return commitsList;
    }

    public static String getCurrentBranch(final String path) throws IOException, NoHeadException {
        Git git = initializeGit(path);
        return git.getRepository().getBranch();
    }

    public static List<String> getBranches(final String path) throws IOException, GitAPIException {
        Git git = initializeGit(path);

        List<Ref> refs = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        List<String> branches = new ArrayList<String>();
        for (Ref r : refs) {
            branches.add(r.getName().replace("refs/heads/", "").replace("refs/remotes/", ""));
        }

        return branches;
    }

    public static void cloneRepository(final String remoteAddress, final String localPath, final ProgressMonitor monitor) throws InvalidRemoteException, TransportException, GitAPIException{
        CloneCommand clone = Git.cloneRepository();
        clone.setBare(false);
        clone.setCloneAllBranches(true);
        clone.setDirectory(new File(localPath));
        clone.setURI(remoteAddress);
        clone.setProgressMonitor(monitor);
        clone.call();
    }

    private static Git initializeGit(final String path) throws IOException, NoHeadException {
        RepositoryBuilder gitRepoBuilder = new RepositoryBuilder();
        Repository gitRepo = gitRepoBuilder.setGitDir(new File(path)).readEnvironment().findGitDir().build();
        Git git = new Git(gitRepo);

        // Make sure path contains a git repository.
        if (!gitRepo.getObjectDatabase().exists()) {
            throw new NoHeadException("Directory " + path + " does not look like a git repository.");
        }

        return git;
    }

    private List<GitCommit> processRepo(final String path, final GitParserOptions gitParserOptions) throws IOException, NoHeadException, GitAPIException{
        Git git = initializeGit(path);
        List<GitCommit> analyzedCommits = new ArrayList<GitCommit>();

        LogCommand log = git.log();

        if (gitParserOptions.hasBranch()) {
            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            Ref branch = null;
            for (Ref b : branches)
            {
                if (b.getName().equals("refs/heads/" + gitParserOptions.getBranch())
                        || b.getName().equals("refs/remotes/" + gitParserOptions.getBranch())) {
                    branch = b;
                    break;
                }
            }
            if (branch == null) {
                throw new IOException("Specified branch was not found in git repository");
            }
            log.add(branch.getObjectId());
        }

        Iterable<RevCommit> loglines = log.call();
        Iterator<RevCommit> logIterator = loglines.iterator();
        while (logIterator.hasNext()){
            RevCommit commit = logIterator.next();
            GitcommitProcessor proc = new GitcommitProcessor(gitParserOptions, git.getRepository());
            proc.setRevCommit(commit);
            proc.processCommitData();
            analyzedCommits.add(proc.getResult());
        }
        return analyzedCommits;
    }


    class GitcommitProcessor {

        private final Pattern PATTERN = Pattern.compile("^(.*) [a-f0-9]{40} (A|C|D|M|R|T)");

        private final GitParserOptions options;
        private final GitCommit analyzedCommit = new GitCommit();
        private final Repository repository;
        private RevCommit revCommit;

        public GitcommitProcessor(final GitParserOptions options, final Repository repository) {
            this.options = options;
            this.repository = repository;
        }

        public void setRevCommit (final RevCommit revcommit){
            this.revCommit = revcommit;
        }

        public GitCommit getResult() {
            return this.analyzedCommit;
        }

        private void processCommitData() throws IOException {
            hash();
            author();
            date();
            message();
            files();
        }

        private void files() throws IOException {
            List<String> filesList= getFilesInCommit(this.repository, this.revCommit);
            for (String fileLine : filesList){
                Matcher matcher = PATTERN.matcher(fileLine);
                if (matcher.matches()) {
                    parsePath(matcher);
                }
            }
        }

        private void message() {
            String message = this.revCommit.getFullMessage();
            if (message.endsWith("\n")){
                message = message.substring(0, message.length()-1);
            }
            this.analyzedCommit.addToMessage(message);
        }

        private void author() {
            this.analyzedCommit.setAuthor(this.revCommit.getAuthorIdent().getName());
        }

        private void date() {
            this.analyzedCommit.setDate(this.revCommit.getAuthorIdent().getWhen());
        }

        private void hash() {
            String[] commitId = this.revCommit.getId().toString().split(" ");
            this.analyzedCommit.setId(commitId[1]);
        }
        
        private void parsePath(final Matcher matcher) {
            String operationCode = matcher.group(2);
            String origin = matcher.group(1);
            String transformed = origin.replaceAll("/", ".");
            
            String parseJavaClass = "";
            if(SCMExtensionsParser.extensionFits(transformed, Arrays.asList("*"))){
            	if(transformed.endsWith(".java")){
    				if(packagePrefixValidate(transformed)){
    					parseJavaClass = parseJavaClass(transformed);
    				}
    			}
				GitCommitFile gitFile = new GitCommitFile();
                gitFile.setRawOperation(operationCode.charAt(0));
                gitFile.setPath(origin);
                gitFile.setExtension(FilenameUtils.getExtension(transformed));
                gitFile.setJavaClass(parseJavaClass);
                this.analyzedCommit.getFiles().add(gitFile);
            }
        }

        private boolean packagePrefixValidate(final String path) {
			if(options.hasPackagePrefix()) {
				return path.indexOf(options.getPackagePrefix()) != -1;
			}
			return false;
        }

        private String parseJavaClass(final String path) {
            String javaClass = path.replace(".java", "");
            if (options.hasPackagePrefix()) {
                javaClass = javaClass.substring(javaClass.indexOf(options.getPackagePrefix()));
            }
            return javaClass;
        }
    }

    //modified method from https://github.com/gitblit/gitblit/blob/master/src/main/java/com/gitblit/utils/JGitUtils.java#L718
    private List<String> getFilesInCommit(final Repository repository, final RevCommit commit) throws IOException {
        RevWalk rw = new RevWalk(repository);
        List<String> filesList = new ArrayList<String>();
        String fileLine = "";

        if (commit.getParentCount() > 1) {
            // Merge commit
        } else if (commit.getParentCount() == 0) {
            TreeWalk tw = new TreeWalk(repository);
            tw.reset();
            tw.setRecursive(true);
            tw.addTree(commit.getTree());
            while (tw.next()) {
                fileLine = tw.getPathString()+" "+tw.getObjectId(0).getName()+" "+this.setOperationSymbol("ADD");
            }
            tw.release();
        } else {
            RevCommit parent = rw.parseCommit(commit.getParent(0).getId());
            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
            df.setRepository(repository);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
            for (DiffEntry diff : diffs) {
                String objectId = diff.getNewId().name();
                if (diff.getChangeType().equals(ChangeType.DELETE)) {
                    fileLine = diff.getOldPath()+" "+objectId+" "+this.setOperationSymbol(diff.getChangeType().name());
                    filesList.add(fileLine);
                } else if (diff.getChangeType().equals(ChangeType.RENAME)) {
                    //in git log there is two operations for RENAME: DELETE old file and ADD new so we need to add also two files to log:
                    fileLine = diff.getOldPath()+" "+objectId+" "+this.setOperationSymbol(ChangeType.DELETE.name());
                    filesList.add(fileLine);
                    fileLine = diff.getNewPath()+" "+objectId+" "+this.setOperationSymbol(ChangeType.ADD.name());
                    filesList.add(fileLine);
                } else {
                    fileLine = diff.getNewPath()+" "+objectId+" "+this.setOperationSymbol(diff.getChangeType().name());
                    filesList.add(fileLine);
                }
            }
        }
        return filesList;
    }

    private char setOperationSymbol(final String operationCode) {
        char op;
        switch (operationCode) {
        case "MODIFY":
            op = 'M';
            break;
        case "ADD":
            op = 'A';
            break;
        case "COPY":
            op = 'C';
            break;
        case "DELETE":
            op = 'D';
            break;
        case "RENAME":
            op = 'R';
            break;
        default:
            op = 'O';
            break;
        }
        return op;
    }

}
