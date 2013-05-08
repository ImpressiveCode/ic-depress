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
        String clearText = "Someclearinput";
        String encryptedText = null;
        String decryptedText = null;

        System.out.print("tekst wejściowy: ");
        System.out.println(clearText);
        System.out.print("klucz: ");
        System.out.println(key);
        encryptedText = CryptographicUtility.useAlgorithm(clearText, key, true);
        System.out.print("zaszyfrowany tekst: ");
        
        System.out.println(encryptedText);
        decryptedText = CryptographicUtility.useAlgorithm(encryptedText, key, false);
        System.out.print("odszyfrowany tekst: ");
        System.out.println(decryptedText);
        assertEquals(clearText, decryptedText);
    }

}
