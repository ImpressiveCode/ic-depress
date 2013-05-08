package org.impressivecode.depress.data.objects.tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.junit.Test;
import org.knime.core.util.crypto.HexUtils;

import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;

public class CryptographicUtilityTest {

    @Test
    public void testGenerateKey() {
        fail("Not yet implemented");
    }

    @Test
    public void testUseAlgorithm() throws UnsupportedEncodingException {
        String key = "12332222";
        String clearText = "Some abcdefghijklmnoprstuvwx   (){}..yz   ";
        clearText = HexUtils.bytesToHex(clearText.getBytes("ASCII"));

        String encryptedText = null;
        String decryptedText = null;

        System.out.print("tekst wejściowy:    ");
        System.out.println(clearText);
        encryptedText = CryptographicUtility.useAlgorithm(clearText, key, true);
        
        decryptedText = CryptographicUtility.useAlgorithm(encryptedText, key, false);

        System.out.print("odszyfrowany tekst: ");
        System.out.println(decryptedText);
        System.out.print("zaszyfrowany tekst: ");
        System.out.println(encryptedText);
        System.out.println(new String(HexUtils.hexToBytes(decryptedText), "ASCII"));
        System.out.print("klucz: ");
        System.out.println(key);
        assertEquals(clearText, decryptedText);
    }

}
