package org.impressivecode.depress.data.objects.tests;

import static org.junit.Assert.*;

import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.junit.Test;

public class CryptographicUtilityTest {

    @Test
    public void testGenerateKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testUseAlgorithm() {
        String key = CryptographicUtility.generateKey();
        String clearText = "Some clear input";
        String encryptedText = null;
        String decryptedText = null;
        
        encryptedText = CryptographicUtility.useAlgorithm(clearText, key, true);
        System.out.println(encryptedText);
        decryptedText = CryptographicUtility.useAlgorithm(encryptedText, key, false);
        System.out.println(decryptedText);
    }

}
