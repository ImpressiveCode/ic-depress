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

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.io.LineProcessor;

/**
 * <code>GitLogParser</code> converts git's log output into appropriate
 * structure.
 * 
 * Expects to receive git log generated using the following command:
 * 
 * git log --pretty=format:"@BEGIN{%H%n%ci%n%an%n%B%n%H}@END" --raw --no-merges --abbrev=40 > log.txt
 * 
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Marek Majchrzak, ImpressiveCode
 */

public class GitLogParser {
    private static Pattern fileCommitRegex = Pattern
            .compile("^:\\d{6} \\d{6} [a-f0-9]{40} [a-f0-9]{40} (A|C|D|M|R|T)\t(.*)$");

    private String lastLine;
    private String currentLine;
    private GitCommit currentCommit;

    public List<GitCommit> parseEntries(final String path) throws IOException, ParseException {
        Preconditions.checkArgument(!isNullOrEmpty(path), "Path has to be set.");

        while (skipEmptyLines()) {
            readNextCommit();
        }
        return commits;
    }

    private class GitLineProcessor implements LineProcessor<List<GitCommit>> {
        final ImmutableList.Builder<GitCommit> builder = ImmutableList.builder();

        @Override
        public List<GitCommit> getResult() {
            return builder.build();
        }

        @Override
        public boolean processLine(final String line) throws IOException {


            return true;
        }

    }

    private void readNextCommit() throws IOException, ParseException {
        parseNewCommit();
        parseDate();
        parseAuthor();
        parseMessage();
        parseFiles();
        // parseMarkers();

        commits.add(currentCommit);
        currentCommit = null;
    }

    private String nextLine() throws IOException {
        if (lastLine != null) {
            currentLine = lastLine;
            lastLine = null;
        } else {
            currentLine = supplier.getInput().lineCounter++;
        }
        return currentLine;
    }

    protected void resetLine() {
        lastLine = currentLine;
    }

    protected Boolean skipEmptyLines() throws IOException {
        String line;
        while ((line = nextLine()) != null && line.isEmpty()) {
        }
        if (line == null) {
            return false;
        }
        resetLine();
        return true;
    }

    protected void parseNewCommit() throws IOException {
        currentCommit = new GitCommit(nextLine());
    }

    protected void parseDate() throws ParseException, IOException {
        currentCommit.setDate(nextLine());
    }

    protected void parseAuthor() throws IOException {
        currentCommit.setAuthor(nextLine());
    }

    protected void parseMessage() throws IOException {
        String line;
        while (!GitCommit.idRegex.matcher((line = nextLine())).matches()) {
            currentCommit.addToMessage(line);
        }
    }

    // Parse commit affected files in the following format:
    // :100644 100644 858e6bda3fbfa25d5d4c3ad5cb16f68c5048ba03
    // ffa8addcc98154f8c84012394c50476e84c9f3bc M
    // ic-depress-scm-git/META-INF/MANIFEST.MF
    protected void parseFiles() throws IOException, ParseException {
        String line;

        while ((line = nextLine()) != null && !line.isEmpty()) {
            Matcher matcher = fileCommitRegex.matcher(line);
            if (matcher.matches()) {
                String operationCode = matcher.group(1);
                String path = matcher.group(2);

                currentCommit.files.add(new GitCommitFile(path, operationCode.charAt(0), currentCommit));
            } else {
                throw new ParseException("Not a valid file commit", lineCounter);
            }
        }
    }

}
