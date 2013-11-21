package it.unitn.hci.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class UniversalBase64Encoder
{

    private UniversalBase64Encoder()
    {
        // static methods only
    }


    public static String encode(byte[] data) throws Exception
    {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }


    public static byte[] decode(String data) throws Exception
    {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }
}
