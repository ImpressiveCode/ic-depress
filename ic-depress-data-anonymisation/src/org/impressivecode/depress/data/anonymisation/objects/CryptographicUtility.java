/*
 ImpressiveCode Depress Framework
 Copyright (C) 2013  ImpressiveCode contributors

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/
>.
 */
package org.impressivecode.depress.data.anonymisation.objects;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.knime.core.util.crypto.HexUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class CryptographicUtility {

    public static boolean isEncrypted(String s) {
        return s.length() > 11 && s.length() % 4 == 0
                && s.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    }

    public static String generateKey() {
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(32 * 5, random).toString(32);
    }

    /**
     * Uses encryption.decryption algorithm depends on encrypt parameter
     * 
     * @param input
     *            input data
     * @param passphrase
     *            Key phrase
     * @param encrypt
     *            Set encryption algorithm into Encrypt/Decrypt state
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String useAlgorithm(String input, String passphrase, boolean encrypt)
            throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Throwable thrown = null;
        byte[] transformed = null;
        int encryptMode = 0;
        MessageDigest md;
        byte[] encodedInput = null;

        // convert passprase to key

        md = MessageDigest.getInstance("MD5");

        md.update(passphrase.getBytes(Charset.defaultCharset()));
        byte[] encodedKey = md.digest();
        Key key = new SecretKeySpec(encodedKey, 0, 8, "DES");

        if (encrypt) {
            // prepare input for encryption
            encryptMode = Cipher.ENCRYPT_MODE;
            input = HexUtils.bytesToHex(input.getBytes(Charset.defaultCharset()));
            // append input length to beggining of the string
            // append some salt to input REQUIRED
            input = input.length() + "/" + input + "salt";
        } else {
            encryptMode = Cipher.DECRYPT_MODE;
        }

        encodedInput = new BASE64Decoder().decodeBuffer(input);

        // finally dencryption :)

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(encryptMode, key);
        transformed = cipher.doFinal(encodedInput);

        String out = new BASE64Encoder().encodeBuffer(transformed);
        out = out.replaceAll("\n", "");

        if (!encrypt) {
            // clean output data
            // trim metadata from beggining and padding signs from the end
            String[] strarray = out.split("/", 2);
            int end = Integer.parseInt(strarray[0]);
            out = strarray[1].substring(0, end);
            out = new String(HexUtils.hexToBytes(out), Charset.defaultCharset());
        }

        return thrown == null ? out : ":(";
    }
}