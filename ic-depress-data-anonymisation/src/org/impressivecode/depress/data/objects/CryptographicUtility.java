package org.impressivecode.depress.data.objects;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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
        
    public static String useAlgorithm(String input, String key)
    {
        try
        {
            byte[] byteKey = key.getBytes();
            SecretKeySpec secretKey = new SecretKeySpec(byteKey, "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(input.getBytes());
            return encrypted.toString();
        } 
        catch (NoSuchPaddingException nspe)
        {
            return input;
        }
        catch (NoSuchAlgorithmException nsae)
        {
            return input;
        }
        catch (InvalidKeyException ike)
        {
            return input;
        }
        catch (BadPaddingException bpe)
        {
            return input;
        }
        catch (IllegalBlockSizeException ibse)
        {
            return input;
        }
    }
}