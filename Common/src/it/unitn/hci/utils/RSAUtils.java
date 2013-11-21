package it.unitn.hci.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import javax.crypto.Cipher;

public class RSAUtils
{

    private RSAUtils()
    {
        // static methods only
    }

    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";


    public static String encode(InputStream key, boolean isKeyPublic, String data) throws Exception
    {
        Key k = rsaKeyFromStream(key, isKeyPublic);
        byte[] byteData = data.getBytes();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, isKeyPublic ? (PublicKey) k : (PrivateKey) k);
        byte[] encryptedByteData = cipher.doFinal(byteData);
        return UniversalBase64Encoder.encode(encryptedByteData);
    }


    public static String decode(InputStream key, boolean isKeyPublic, String data) throws Exception
    {
        Key k = rsaKeyFromStream(key, isKeyPublic);
        byte[] byteData = UniversalBase64Encoder.decode(data);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, isKeyPublic ? (PublicKey) k : (PrivateKey) k);
        byte[] decryptedByteData = cipher.doFinal(byteData);
        return new String(decryptedByteData);
    }


    private static Key rsaKeyFromStream(InputStream is, boolean isPublic) throws Exception
    {
        ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(is));
        try
        {
            BigInteger m = (BigInteger) oin.readObject();
            BigInteger e = (BigInteger) oin.readObject();
            if (isPublic)
            {
                RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PublicKey pubKey = fact.generatePublic(keySpec);
                return pubKey;
            }
            else
            {
                RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PrivateKey privKey = fact.generatePrivate(keySpec);
                return privKey;
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Spurious serialisation error", e);
        }
        finally
        {
            oin.close();
        }
    }
}
