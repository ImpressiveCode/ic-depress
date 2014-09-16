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
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.impressivecode.depress.scm.SCMExtensionsParser;
import org.impressivecode.depress.scm.SCMParserOptions;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

/**
 * <code>GitLogParser</code> converts git's log output into appropriate
 * structure.
 * 
 * Expects to receive git log generated using the following command:
 * 
 * git log --pretty=format:"%H%n%ct%n%an%n%B%n%H" --raw --no-merges --abbrev=40
 * 
 * 
 * @author Tomasz Kuzemko
 * @author Sławomir Kapłoński
 * @author Marek Majchrzak, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class GitOfflineLogParser {
    final SCMParserOptions parserOptions;

    public GitOfflineLogParser(final SCMParserOptions parserOptions) {
        this.parserOptions = checkNotNull(parserOptions, "Options has to be set");
    }

    public List<GitCommit> parseEntries(final String path) throws IOException, ParseException {
        checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        return Files.readLines(new File(path), Charsets.UTF_8, new GitLineProcessor(parserOptions));
    }

    static class GitLineProcessor implements LineProcessor<List<GitCommit>> {

        private enum LEXER {
            HASH, DATE, AUTHOR, MSG, FILES
        }

        private final static Pattern PATTERN = Pattern
                .compile("^:\\d{6} \\d{6} [a-f0-9]{40} [a-f0-9]{40} (A|C|D|M|R|T)\t(.*)$");

        final ImmutableList.Builder<GitCommit> builder = ImmutableList.builder();
        private SCMParserOptions options;
        private GitCommit commit;
        private LEXER nextStep = LEXER.HASH;
        private long counter = 0;

        public GitLineProcessor(final SCMParserOptions options) {
            this.options = options;
        }

        @Override
        public List<GitCommit> getResult() {
            return builder.build();
        }

        @Override
        public boolean processLine(final String line) throws IOException {
            counter++;
            try {
                processLinIntern(line);
                return true;
            } catch (Exception e) {
                String message = String.format("Unable to parse line: [%s], line number:[%s]", line, counter);
                throw new IllegalArgumentException(message, e);
            }
        }

        private void processLinIntern(final String line) {
            switch (nextStep) {
            case AUTHOR:
                author(line);
                break;
            case DATE:
                date(line);
                break;
            case FILES:
                files(line);
                break;
            case HASH:
                hash(line);
                break;
            case MSG:
                message(line);
                break;
            }
        }

        private void files(final String line) {
            if (Strings.isNullOrEmpty(line)) {
                this.nextStep = LEXER.HASH;
            } else {
                Matcher matcher = PATTERN.matcher(line);
                if (matcher.matches()) {
                    parsePath(matcher);
                } else {
                    // no changed files, new entry analysis have to be started
                    // immediately
                    hash(line);
                }
            }
        }

        private void message(final String line) {
            if (this.commit.getId().equals(line)) {
                this.nextStep = LEXER.FILES;
            } else if (!Strings.isNullOrEmpty(line)) {
                this.commit.addToMessage(line);
            }
        }

        private void author(final String line) {
            this.commit.setAuthor(line);
            this.nextStep = LEXER.MSG;
        }

        private void date(final String line) {
            this.commit.setDate(new Date(Long.parseLong(line) * 1000));
            this.nextStep = LEXER.AUTHOR;
        }

        private void hash(final String line) {
            this.commit = new GitCommit();
            this.commit.setId(line);
            this.builder.add(commit);
            this.nextStep = LEXER.DATE;
        }

        private void parsePath(final Matcher matcher) {
            String operationCode = matcher.group(1);
            String origin = matcher.group(2);
            String transformed = origin.replaceAll("/", ".");
            
            String parseJavaClass = "";
            if (SCMExtensionsParser.extensionFits(transformed, options.getExtensionsNamesToFilter())) {
                GitCommitFile gitFile = new GitCommitFile();
                gitFile.setRawOperation(operationCode.charAt(0));
                gitFile.setPath(origin);
                gitFile.setExtension(FilenameUtils.getExtension(transformed));
                gitFile.setJavaClass(parseJavaClass);
                if (transformed.endsWith(".java")) {
                    if (packagePrefixValidate(transformed)) {
                        gitFile.setJavaClass(parseJavaClass(transformed));
                        commit.getFiles().add(gitFile);
                    }
                } else {
                    commit.getFiles().add(gitFile);
                }
            }
        }

        private boolean packagePrefixValidate(final String path) {
            if (options.hasPackagePrefix()) {
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
}
