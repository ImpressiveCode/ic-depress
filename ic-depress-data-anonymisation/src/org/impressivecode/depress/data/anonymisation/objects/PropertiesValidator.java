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
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */
package org.impressivecode.depress.data.anonymisation.objects;

import java.io.File;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

public class PropertiesValidator {

    /**
     * Checks if supplied path to key file is valid
     * 
     * @param path full path of the required key file
     * @return binominal status of file validity
     * @throws InvalidSettingsException
     */
    public static final boolean isKeyFileCorrect(String path) throws InvalidSettingsException {
        File keyFile = new File(path);
        if (!keyFile.exists()) {
            throw new InvalidSettingsException("Key File does not exist!");
        }
        if (!keyFile.isFile()) {
            throw new InvalidSettingsException("Key File is not a file!");
        }
        return true;
    }
    
    /**
     * Checks if node configuration is correct
     * 
     * @param config Node configuration instance
     * @return binominal status of column validity
     * @throws InvalidSettingsException
     */
    public final static boolean columnsCheck(final Config config) throws InvalidSettingsException {

        Config includeColumns = config.getConfig("InclList");
        Config excludeColumns = config.getConfig("ExclList");
        boolean isValid = false;

//      FIXME: Co w przypadku kiedy nie zawiera tych kluczy? False czy Exeption? 
        if (includeColumns.containsKey("array-size") && excludeColumns.containsKey("array-size")) {
            int includeSize = includeColumns.getInt("array-size");
            int excludeSize = excludeColumns.getInt("array-size");
            if (includeSize == 0 && excludeSize == 0) {
                throw new InvalidSettingsException("Input Table cannot be empty!");
            }
            else
            {
                isValid = true;
            }
        }
        return isValid;
    }
    
}
