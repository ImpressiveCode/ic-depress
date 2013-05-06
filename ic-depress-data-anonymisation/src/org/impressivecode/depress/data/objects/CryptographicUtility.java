package org.impressivecode.depress.data.objects;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class CryptographicUtility {
    
    public static String generateKey()
    {
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
            SecretKey secretkey = keygenerator.generateKey();    
            return secretkey.getEncoded().toString().substring(3);
        }
        catch (NoSuchAlgorithmException e)
        {
            return "";
        }
    }
        
    public static String useAlgorithm(String input, String key, boolean encrypt)
    {
        Throwable thrown = null;
        byte[] transformed = null;
        byte[] byteKey = key.getBytes();
        int encryptMode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        SecretKeySpec secretKey = new SecretKeySpec(byteKey, "Blowfish");

        try
        {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(encryptMode, secretKey);
            transformed = cipher.doFinal(input.getBytes());
        } 
        catch (Exception e)
        {
            thrown = e;
        }
        
        return thrown==null ? new String(transformed) : ":(";
    }
}