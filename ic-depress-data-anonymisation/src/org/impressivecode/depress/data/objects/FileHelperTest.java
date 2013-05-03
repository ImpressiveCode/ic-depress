package org.impressivecode.depress.data.objects;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FileHelperTest {

    /**
     * Checks if invalid filename will generate filename according to default settings.
     */
    @Test
    public void testCreateTmpFileInvalidFilename() {
        String expected = FileHelper.TMP_DIR + FileHelper.DIR_SEPARATOR
                + FileHelper.KEY_FILENAME + "-" + 0 + "." + FileHelper.KEY_FILENAME_EXT;
        File f = new File(expected);
        f.delete();
        assertEquals(FileHelper.CreateTmpFile(""), expected);
    }

    /**
     * Checks if valid filename will generate filename according to default settings.
     */
    @Test
    public void testCreateTmpFileValidFilename() {
        String fname = "validName";
        String expected = FileHelper.TMP_DIR + FileHelper.DIR_SEPARATOR
                + fname + "-" + 0 + "." + FileHelper.KEY_FILENAME_EXT;
        File f = new File(expected);
        f.delete();
        assertEquals(FileHelper.CreateTmpFile(fname), expected);
    }

    /**
     * Checks if valid filename will generate filename with iterated number
     * according to default settings.
     */
    @Test
    public void testCreateTmpFileSameValidFilenames() throws IOException {
        String fname = "validName";
        String expected = FileHelper.TMP_DIR + FileHelper.DIR_SEPARATOR
                + fname + "-" + 1 + "." + FileHelper.KEY_FILENAME_EXT;
        String fpath = FileHelper.CreateTmpFile(fname);
        File f = new File(fpath);
        f.createNewFile();
        f.deleteOnExit();
        fpath = FileHelper.CreateTmpFile(fname);
        assertEquals(fpath, expected);
    }

    /**
     * Checks if file is written without errors given correct parameters
     */
    @Test
    public void testWriteToFileSchouldSucceed() {
        Throwable thrown = null;
        String fp = FileHelper.CreateTmpFile(FileHelper.KEY_FILENAME);
        File f = new File(fp);
        f.deleteOnExit();
        
        try {
            FileHelper.WriteToFile(fp, FileHelper.KEY_FILENAME);
        }
        catch (IOException e)
        {
            thrown = e;
        }
        assertNull(thrown);
    }

    @Test
    public void testReadFromFile() {
        fail("Not yet implemented");
    }

}
