package org.impressivecode.depress.data.objects;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.xmlbeans.impl.util.Base64;
import org.impressivecode.depress.data.anonymisation.AnonymisationNodeModel;
import org.knime.core.util.crypto.HexUtils;

import com.sun.xml.internal.fastinfoset.algorithm.HexadecimalEncodingAlgorithm;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class CryptographicUtility {
    
    public static String generateKey()
    {
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(AnonymisationNodeModel.KEY_LENGTH * 5, random).toString(32);
    }
        
    public static String useAlgorithm(String input, String passphrase, boolean encrypt)
    {
//        try {
//            input = encrypt ? String.format("%040x", new BigInteger(input.getBytes("UTF-8"))) : input;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
        Throwable thrown = null;
        byte[] transformed = null;
        int encryptMode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        if(encrypt)
        {
            try {
                input = HexUtils.bytesToHex(input.getBytes("ASCII"));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                //            e1.printStackTrace();
            }
        }
        
        byte[] encodedKey = null;
        byte[] encodedInput = null;
        try {
            passphrase = new BASE64Encoder().encodeBuffer(passphrase.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            encodedKey = new BASE64Decoder().decodeBuffer(passphrase);
            encodedInput = new BASE64Decoder().decodeBuffer(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Key key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "DES");

        try
        {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(encryptMode, key);
            transformed = cipher.doFinal(encodedInput);
        } 
        catch (Exception e)
        {   
            e.printStackTrace();
            thrown = e;
            throw new RuntimeException(e);
        }
        
        String out = new BASE64Encoder().encodeBuffer(transformed);
//        if(!encrypt)
//        {
//            StringBuilder output = new StringBuilder();
//            for (int i = 0; i < input.length(); i+=2) {
//                String str = input.substring(i, i+2);
//                output.append((char)Integer.parseInt(str, 16));
//            }
//            out = output.toString();
//        }
        return thrown==null ? out : ":(";
    }
}