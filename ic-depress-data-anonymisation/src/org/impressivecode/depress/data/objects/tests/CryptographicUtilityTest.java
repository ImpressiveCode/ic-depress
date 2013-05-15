package org.impressivecode.depress.data.objects.tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;

import javax.sound.sampled.AudioFormat.Encoding;

import org.impressivecode.depress.data.objects.CryptographicUtility;
import org.junit.Test;
import org.knime.core.util.crypto.HexUtils;

import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;

public class CryptographicUtilityTest {

//    @Test
//    public void testGenerateKey() {
//        fail("Not yet implemented");
//    }

    @Test
    public void testUseAlgorithm() throws UnsupportedEncodingException {
        String key = "some password";
        String clearText = "Some aa";

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
        System.out.print("czy zaszyfrowany: ");
        System.out.println(CryptographicUtility.isEncrypted(encryptedText));
        System.out.println();
        System.out.print("klucz: ");
        System.out.println(key);
        assertEquals(clearText, decryptedText);
    }

}
