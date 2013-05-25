package org.impressivecode.depress.data.objects;

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
