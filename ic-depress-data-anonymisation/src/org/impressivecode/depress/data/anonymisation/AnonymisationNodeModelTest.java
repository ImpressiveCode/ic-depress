package org.impressivecode.depress.data.anonymisation;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

public class AnonymisationNodeModelTest {

    @Test
    public void test() {
        assertTrue("CI - testing if tests works", true);
    }

//  metod prywatnych i chronionych ię nie testuje...
//  zakomentowany, poprawiony kod zostawiłem w celach edukacyjnych
    
//    @Test
//    public void isKeyFileCorrectFileDoesNotExistTest()
//    {
//        String tmpdir = System.getProperty("java.io.tmpdir");
//        File testFile = null;
//        Throwable caught = null;
//        do
//        {
//            String tmpfilename = Long.toHexString(Double.doubleToLongBits(Math.random()));
//            testFile = new File(tmpdir + System.getProperty("file.separator") + tmpfilename);
//        }
//        while(testFile.exists());
//
//        try
//        {
//            AnonymisationNodeModel.isKeyFileCorrect(testFile.getAbsolutePath());
//        }
//        catch (InvalidSettingsException e)
//        {
//            caught = e;
//        }
//
//        assertNotNull(caught);
//        assertSame(InvalidSettingsException.class, caught.getClass());
//     }
//    
//    @Test
//    public void isKeyFileCorrectFileIsDirectoryTest()
//    {
//        String tmpdir = System.getProperty("java.io.tmpdir");
//        Throwable caught = null;
//        try
//        {
//            AnonymisationNodeModel.isKeyFileCorrect(tmpdir);
//        }
//        catch (InvalidSettingsException e)
//        {
//            caught = e;
//        }
//
//        assertNotNull(caught);
//        assertSame(InvalidSettingsException.class, caught.getClass());
//    }

}
