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
package org.impressivecode.depress.support.sourcecrawler;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.maven.plugin.MojoExecutionException;
import org.impressivecode.utils.sourcecrawler.SourceCrawlerScanner;
import org.impressivecode.utils.sourcecrawler.model.JavaClazz;
import org.impressivecode.utils.sourcecrawler.model.JavaFile;

/**
 * @author Pawel Nosal, ImpressiveCode
 * @author Maciej Borkowski, Capgemini Poland
 */
public class CrawlerEntriesParser {
    public SourceCrawlerOutput parseFromExecutableJar(final String path, boolean createXML) throws IOException,
            MojoExecutionException, JAXBException {
        SourceCrawlerScanner sourceCrawlerScanner = new SourceCrawlerScanner();
        List<JavaFile> javaFiles;
        try {
            javaFiles = sourceCrawlerScanner.executeCleanly(path);
        } catch (IOException e) {
            throw new IOException("Could not find java files");
        }
        if (createXML) {
            String output = new File(path).getName() + ".xml";
            sourceCrawlerScanner.writeXMLDocument(javaFiles, output);
        }

        SourceCrawlerOutput result = new SourceCrawlerOutput();
        ArrayList<SourceFile> sourceFiles = new ArrayList<SourceFile>();
        for (JavaFile javaFile : javaFiles) {
            sourceFiles.add(parseJavaFile(javaFile));
        }
        result.setSourceFiles(sourceFiles);

        return result;
    }

    private SourceFile parseJavaFile(final JavaFile javaFile) {
        SourceFile sourceFile = new SourceFile();
        ArrayList<Clazz> clazzes = new ArrayList<Clazz>();
        for (JavaClazz javaClazz : javaFile.getClasses()) {
            clazzes.add(parseJavaClazz(javaClazz));
        }
        sourceFile.setClasses(clazzes);
        sourceFile.setPath(javaFile.getFilePath());
        sourceFile.setSourcePackage(javaFile.getPackageName());
        return sourceFile;
    }

    private Clazz parseJavaClazz(final JavaClazz javaClazz) {
        Clazz clazz = new Clazz();
        clazz.setAccess(javaClazz.getClassAccess().getAccess());
        clazz.setException(javaClazz.isException());
        clazz.setFinal(javaClazz.isFinal());
        clazz.setInner(javaClazz.isInner());
        clazz.setName(javaClazz.getClassName());
        clazz.setTest(javaClazz.isTest());
        clazz.setType(javaClazz.getClassType().getName());
        return clazz;
    }

    public SourceCrawlerOutput parseFromXML(final String path) throws JAXBException {
        checkArgument(!isNullOrEmpty(path), "Path has to be set.");
        JAXBContext jaxbContext;
        jaxbContext = JAXBContext.newInstance(SourceCrawlerOutput.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        SourceCrawlerOutput result;
        try {
            result = (SourceCrawlerOutput) unmarshaller.unmarshal(new File(path));
        } catch (JAXBException e) {
            throw new JAXBException("Could not parse file, unmarshaller failed");
        }
        return result;
    }

    public List<Clazz> parseClassesFromFile(SourceFile sourceFile) {
        return sourceFile.getClasses();
    }

}
