package org.impressivecode.depress.test.scm.svn;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.impressivecode.depress.scm.SCMDataType;
import org.impressivecode.depress.scm.svn.SVNOfflineLogParser;
import org.impressivecode.depress.scm.SCMOperation;
import org.impressivecode.depress.scm.SCMParserOptions;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class SVNOfflineLogParserTest {

    private final static String logFilePath = SVNOfflineLogParserTest.class.getResource("svn.xml").getPath();
    private final static String logFilePathMerged = SVNOfflineLogParserTest.class.getResource("svn_merged.xml").getPath();

    @Test
    public void shouldParseJavaEntries() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add(".java");
        SCMParserOptions options = SCMParserOptions.options("", extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);
        // then
        assertThat(entries).hasSize(27);
    }

    @Test
    public void shouldParseJavaEntriesMerged() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add(".java");
        SCMParserOptions options = SCMParserOptions.options("", extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePathMerged);
        // then
        assertThat(entries).hasSize(27);
    }

    @Test
    public void shouldParseGivenEntry() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add(".j?va");
        SCMParserOptions options = SCMParserOptions.options("org.", extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);
        List<SCMDataType> revision = Lists.newArrayList(Iterables.filter(entries, new Predicate<SCMDataType>() {
            @Override
            public boolean apply(final SCMDataType elem) {
                return elem.getCommitID().equals("936295");
            }
        }));

        // then
        assertThat(revision).hasSize(9);
        SCMDataType entry = Iterables.find(revision, new Predicate<SCMDataType>() {
            @Override
            public boolean apply(final SCMDataType scm) {
                return scm.getPath().endsWith("PolynomialFitter.java");
            }
        });
        assertThat(entry.getAuthor()).isEqualTo("erans");
        assertThat(entry.getCommitDate()).isNotNull();
        assertThat(entry.getCommitID()).isEqualTo("936295");
        assertThat(entry.getMessage()).isEqualTo("sdsd MATH-365 sdsdsd");
        assertThat(entry.getOperation()).isEqualTo(SCMOperation.MODIFIED);
        assertThat(entry.getPath())
                .isEqualTo(
                        "/commons/proper/math/trunk/src/main/java/org/apache/commons/math/optimization/fitting/PolynomialFitter.java");
        assertThat(entry.getResourceName()).isEqualTo("org.apache.commons.math.optimization.fitting.PolynomialFitter");
    }

    @Test
    public void shouldParseAnyEntry() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add("*");
        SCMParserOptions options = SCMParserOptions.options("org.", extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);

        // then
        assertThat(entries).hasSize(32);
        SCMDataType entry = Iterables.find(entries, new Predicate<SCMDataType>() {
            @Override
            public boolean apply(final SCMDataType scm) {
                return scm.getPath().endsWith("analysis.xml");
            }
        });
        assertThat(entry.getAuthor()).isEqualTo("erans");
        assertThat(entry.getCommitDate()).isNotNull();
        assertThat(entry.getCommitID()).isEqualTo("936295");
        assertThat(entry.getMessage()).isEqualTo("sdsd MATH-365 sdsdsd");
        assertThat(entry.getOperation()).isEqualTo(SCMOperation.MODIFIED);
        assertThat(entry.getPath()).isEqualTo("/commons/proper/math/trunk/src/site/xdoc/userguide/analysis.xml");

    }

    @Test
    public void shouldNotParseWithWrongPackage() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add("*");
        SCMParserOptions options = SCMParserOptions.options("de.", extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);

        // then
        assertThat(entries).hasSize(5);
        for (SCMDataType entry : entries) {
            assertThat(entry.getExtension()).isNotEqualTo(".java");
        }

    }

    @Test
    public void shouldParseNullPackage() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        extensionsToFilter.add("*");
        SCMParserOptions options = SCMParserOptions.options(null, extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);

        // then
        assertThat(entries).hasSize(32);
    }

    @Test
    public void shouldParseWithNoExtensionsToFilter() throws JAXBException, CloneNotSupportedException {
        // given
        ArrayList<String> extensionsToFilter = new ArrayList<>();
        SCMParserOptions options = SCMParserOptions.options(null, extensionsToFilter);
        SVNOfflineLogParser parser = new SVNOfflineLogParser(options);
        // when
        List<SCMDataType> entries = parser.parseEntries(logFilePath);

        // then
        assertThat(entries).hasSize(0);
    }
}
