package org.impressivecode.depress.data.anonymisation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.knime.core.node.InvalidSettingsException;

public class AnonymisationNodeModelTest {

    @Test
    public void test() {
        assertTrue("CI - testing if tests works", true);
    }
    
    @Test
    public void isKeyFileExistTest(){
        try {
            assertFalse("Key file existing test", AnonymisationNodeModel.isKeyFileCorrect("C:\\file_that_doesnt_exist.blank"));
        } catch (InvalidSettingsException e) {
            e.printStackTrace();
        }
     }
    
    @Test
    public void isKeyFileDirectoryTest(){
        try {
            assertFalse("Key file is not directory", AnonymisationNodeModel.isKeyFileCorrect("C:\\Windows"));
        } catch (InvalidSettingsException e) {
            e.printStackTrace();
        }
     }

}
