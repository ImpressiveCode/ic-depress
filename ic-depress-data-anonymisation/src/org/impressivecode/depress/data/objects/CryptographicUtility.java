package org.impressivecode.depress.data.objects;

import java.security.InvalidAlgorithmParameterException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class CryptographicUtility {
    
    @SuppressWarnings("finally")
    public static String GenerateKey()
    {
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("Blowfish");
            SecretKey secretkey = keygenerator.generateKey();
            return secretkey.toString();
        }
        finally
        {
            return "";
        }
    }
    
    public static String UseAlgorithm(Object input, String key)
    {
        String output = BlowfishAlgorithm(input.toString(), key);
        return output;
    }
        
    @SuppressWarnings("finally")
    private static String BlowfishAlgorithm(String input, String key)
    {
        try
        {
            if (ValidateKey(key))
            {
                byte[] byteKey = key.getBytes();
                SecretKeySpec secretKey = new SecretKeySpec(byteKey, "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encrypted = cipher.doFinal(input.getBytes());
                return encrypted.toString();
            }
            else
            { 
                throw new InvalidAlgorithmParameterException(); 
            }
        }
        finally
        {
            return input;
        } 
    }
    
    private static Boolean ValidateKey(String key)
    {
        switch(key.length())
        {
            case 8:
                return true;
            case 16:
                return true;
            case 24:
                return true;
            case 28:
                return true;
            case 32: 
                return true;
            case 48:
                return true;
            default:
                return false;
        }
    }    
}