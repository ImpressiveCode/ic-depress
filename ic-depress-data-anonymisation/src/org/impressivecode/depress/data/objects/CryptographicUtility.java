package org.impressivecode.depress.data.objects;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.knime.core.util.crypto.HexUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public abstract class CryptographicUtility {
    
    public static boolean isEncrypted(String s) {
        return s.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");
    }
    
    public static String generateKey()
    {
        SecureRandom random = new SecureRandom();
        return new java.math.BigInteger(32 * 5, random).toString(32);
    }
        
    public static String useAlgorithm(String input, String passphrase, boolean encrypt)
    {
        Throwable thrown = null;
        byte[] transformed = null;
        int encryptMode = 0;
        MessageDigest md;
        byte[] encodedInput = null;

        // convert passprase to key
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
        md.update(passphrase.getBytes(Charset.defaultCharset()));
        byte[] encodedKey = md.digest();
        Key key = new SecretKeySpec(encodedKey, 0, 8, "DES");
        
        if(encrypt)
        {
            // prepare input for encryption
            encryptMode = Cipher.ENCRYPT_MODE;
            input = HexUtils.bytesToHex(input.getBytes(Charset.defaultCharset()));
            // append input length to beggining of the string
            // append some salt to input REQUIRED
            input = input.length() + "/" + input + "salt";
        }
        else
        {
            encryptMode = Cipher.DECRYPT_MODE;
        }
        
        try {
            encodedInput = new BASE64Decoder().decodeBuffer(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // finally dencryption :)
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
        out = out.replaceAll("\n", "");

        if(!encrypt)
        {
            // clean output data
            // trim metadata from beggining and padding signs from the end
            String[] strarray = out.split("/",2);
            int end = Integer.parseInt(strarray[0]);
            out = strarray[1].substring(0, end);
            out = new String(HexUtils.hexToBytes(out), Charset.defaultCharset());
        }

        return thrown==null ? out : ":(";
    }
}