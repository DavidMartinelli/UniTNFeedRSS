package it.unitn.hci.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSAKeyGenerator
{

    private static final String ALGORITHM = "RSA";


    public static void generate(String publicPath, String privatePath) throws Exception
    {
        // server keys
        System.out.println("Generating server keys...");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(4096);
        KeyPair kp = kpg.genKeyPair();
        KeyFactory fact = KeyFactory.getInstance(ALGORITHM);
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);

        BigInteger mod = pub.getModulus();
        BigInteger exp = pub.getPublicExponent();
        saveToFile(publicPath, mod, exp);
        saveToFile(privatePath, priv.getModulus(), priv.getPrivateExponent());
    }


    private static void saveToFile(String fileName, BigInteger mod, BigInteger exp)
    {
        ObjectOutputStream oout = null;
        try
        {
            oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            oout.writeObject(mod);
            oout.writeObject(exp);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            try
            {
                if (oout != null)
                {
                    oout.close();
                }
            }
            catch (Exception ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }
    
    public static void main(String[] arg) throws Exception
    {
        System.out.println("Generating RSA key pair...");
        generate("public.key", "private.key");
        System.out.println("done!");
    }
}
