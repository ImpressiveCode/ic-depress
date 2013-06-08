package org.implessivecode.depress.tests.anonymisation;

//import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.data.anonymisation.objects.FileHelper;
import org.impressivecode.depress.data.anonymisation.objects.PropertiesValidator;
import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

public class PropertiesValidatorTest {

    @Test(expected=InvalidSettingsException.class)
    public void testIsKeyFileCorrectFilenameIsDirectory() throws InvalidSettingsException {
        String catalog = FileHelper.TMP_DIR;
        PropertiesValidator.isKeyFileCorrect(catalog);
    }

    @Test(expected=InvalidSettingsException.class)
    public void testIsKeyFileCorrectFilenameDoesNotExist() throws InvalidSettingsException {
        String filepath = FileHelper.CreateTmpFile("non-existing-file");
        PropertiesValidator.isKeyFileCorrect(filepath);
    }

    @Test
    public void testIsKeyFileCorrectSouldSucceed() throws InvalidSettingsException, IOException {
        String fp = FileHelper.CreateTmpFile("non-existing-file");
        File f = new File(fp);
        f.createNewFile();
        f.deleteOnExit();
        PropertiesValidator.isKeyFileCorrect(fp);
    }

    
    /**
     * Checks if exception is thrown in case of invalid configuration parameter count
     * 
     * @throws InvalidSettingsException
     */
    /*
    @Test(expected=InvalidSettingsException.class)
    public void testColumnsCheckInvalidConfiguration() throws InvalidSettingsException {
        Config mockedIncludeColumns = mock(Config.class);
        when(mockedIncludeColumns.containsKey("array-size")).thenReturn(true);
        when(mockedIncludeColumns.getInt("array-size")).thenReturn(0);
        
        Config mockedExcludeColumns = mock(Config.class);
        when(mockedExcludeColumns.containsKey("array-size")).thenReturn(true);
        when(mockedExcludeColumns.getInt("array-size")).thenReturn(0);

        Config mockedConfig = mock(Config.class);
        when(mockedConfig.getConfig("InclList")).thenReturn(mockedIncludeColumns);
        when(mockedConfig.getConfig("ExclList")).thenReturn(mockedExcludeColumns);
    }*/

}
