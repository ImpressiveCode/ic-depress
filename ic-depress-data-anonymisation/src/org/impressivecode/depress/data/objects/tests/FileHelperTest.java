package org.impressivecode.depress.data.objects.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.impressivecode.depress.data.objects.FileHelper;
import org.junit.Test;

public class FileHelperTest {

    @Test
    public void CreateTmpFile(){
        String path = FileHelper.CreateTmpFile("testFile");
        assertTrue(new File(path).exists());
    }
    
    @Test
    public void GenerateKeyFile(){
        try {
            File key = new File(FileHelper.GenerateKeyFile("testKeyFile"));
            assertTrue(key.exists());
            assertTrue(key.getTotalSpace() > 0);
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Test
    public void WriteToFile(){
        File f = new File(FileHelper.CreateTmpFile("TestFile"));
        long sizeBefore = f.length();
        
        try {
            FileHelper.WriteToFile(f.getAbsolutePath(), "Text to test");
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
        assertTrue(sizeBefore < f.length());
    }
    
    @Test
    public void ReadFromFile(){
        String text = "Text to test";
        File f = new File(FileHelper.CreateTmpFile("TestFile"));
        try {
            
            FileHelper.WriteToFile(f.getAbsolutePath(), text);
            assertArrayEquals(text.toCharArray(),FileHelper.ReadFromFile(f.getAbsolutePath()).toCharArray());
            
        } catch (IOException e) {
            fail(e.getMessage());
            e.printStackTrace();
        }
    }
}
