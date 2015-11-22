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

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Maciej Borkowski, Capgemini Poland
 */
public class CrawlerOptionsParser {
    private HashMap<String, Boolean> booleanSettings;
    private String acceptPackageName;

    public CrawlerOptionsParser(final HashMap<String, Boolean> currentSettings, final String acceptedPackage) {
        booleanSettings = currentSettings;
        acceptPackageName = acceptedPackage;
    }

    public void checkRequirements(final SourceCrawlerOutput input) {
        for (Iterator<SourceFile> fileIterator = input.getSourceFiles().iterator(); fileIterator.hasNext();) {
            SourceFile file = fileIterator.next();
            if (!checkPackage(file.getSourcePackage())) {
                fileIterator.remove();
                continue;
            }
            for (Iterator<Clazz> clazzIterator = file.getClasses().iterator(); clazzIterator.hasNext();)
                if (!checkClazz(clazzIterator.next())) {
                    clazzIterator.remove();
                }
            if (file.getClasses().isEmpty()) {
                fileIterator.remove();
            }
        }
    }

    private boolean checkClazz(final Clazz clazz) {
        if (!checkAccess(clazz.getAccess()))
            return false;
        if (!checkType(clazz.getType()))
            return false;
        if (!booleanSettings.get(CrawlerAdapterNodeModel.PUBLIC) && clazz.isException())
            return false;
        if (!booleanSettings.get(CrawlerAdapterNodeModel.INNER) && clazz.isInner())
            return false;
        if (!booleanSettings.get(CrawlerAdapterNodeModel.TEST) && clazz.isTest())
            return false;
        if (!booleanSettings.get(CrawlerAdapterNodeModel.FINAL) && clazz.isFinal())
            return false;
        return true;
    }

    private boolean checkAccess(final String access) {
        if (access.equals(CrawlerAdapterNodeModel.PUBLIC) && booleanSettings.get(CrawlerAdapterNodeModel.PUBLIC))
            return true;
        if (access.equals(CrawlerAdapterNodeModel.PRIVATE) && booleanSettings.get(CrawlerAdapterNodeModel.PRIVATE))
            return true;
        if (access.equals(CrawlerAdapterNodeModel.PROTECTED) && booleanSettings.get(CrawlerAdapterNodeModel.PROTECTED))
            return true;
        if (access.equals(CrawlerAdapterNodeModel.PACKAGE) && booleanSettings.get(CrawlerAdapterNodeModel.PACKAGE))
            return true;
        return false;
    }

    private boolean checkType(final String type) {
        if (type.equals(CrawlerAdapterNodeModel.CLASS) && booleanSettings.get(CrawlerAdapterNodeModel.CLASS))
            return true;
        if (type.equals(CrawlerAdapterNodeModel.INTERFACE) && booleanSettings.get(CrawlerAdapterNodeModel.INTERFACE))
            return true;
        if (type.equals(CrawlerAdapterNodeModel.ABSTRACT) && booleanSettings.get(CrawlerAdapterNodeModel.ABSTRACT))
            return true;
        if (type.equals(CrawlerAdapterNodeModel.ENUM) && booleanSettings.get(CrawlerAdapterNodeModel.ENUM))
            return true;
        return false;
    }

    private boolean checkPackage(final String packageName) {
        return packageName.contains(acceptPackageName);
    }

}
