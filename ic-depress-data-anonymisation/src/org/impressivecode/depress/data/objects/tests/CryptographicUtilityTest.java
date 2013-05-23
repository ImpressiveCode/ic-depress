package org.impressivecode.depress.data.objects.tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.junit.Test;

public class CryptographicUtilityTest {

//    @Test
//    public void testGenerateKey() {
//        fail("Not yet implemented");
//    }

    @Test
    public void testUseAlgorithm() throws UnsupportedEncodingException {
        String key = "sd";
        String clearText = "";
        
        String encryptedText = null;
        String decryptedText = null;

        encryptedText = CryptographicUtility.useAlgorithm(clearText, key, true);
        decryptedText = CryptographicUtility.useAlgorithm(encryptedText, key, false);

        System.out.print("tekst wejściowy:    ");
        System.out.println(clearText);
        System.out.print("czy zaszyfrowany: ");
        System.out.println(CryptographicUtility.isEncrypted(clearText));
        System.out.print("odszyfrowany tekst: ");
        System.out.println(decryptedText);
        System.out.print("zaszyfrowany tekst: ");
        System.out.println(encryptedText);
        System.out.println(encryptedText.length());
        System.out.print("czy zaszyfrowany: ");
        System.out.println(CryptographicUtility.isEncrypted(encryptedText));
        System.out.println();
        System.out.print("klucz: ");
        System.out.println(key);
        assertEquals(clearText, decryptedText);
    }

}