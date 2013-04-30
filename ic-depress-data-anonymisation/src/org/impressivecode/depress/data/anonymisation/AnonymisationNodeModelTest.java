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
    
    @Test
    public void columnsCheckTest()
    {
        /* 
         * XXX 
         * Doesnt working test suggestion
         * 
        Config conf = new Config(AnonymisationNodeModel.COLUMNS) {

            @Override
            public Config getInstance(String key) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        try {
            String incStr = "InclList";
            conf.addConfig(incStr);
            conf.getConfig(incStr).addInt("array-size", 0);
            String exclStr = "ExclList";
            conf.addConfig(exclStr);
            conf.getConfig(exclStr).addInt("array-size", 0);
            assertTrue("Input list is not empty", AnonymisationNodeModel.columnsCheck(conf));
        } catch (InvalidSettingsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        
        //Always false to remember make this test
        assertTrue(false);
    }

}
