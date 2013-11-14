package it.unitn.hci.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileUtis
{
    private FileUtis()
    {
        // static methods only
    }


    public static void writeToFile(String path, String what, boolean append)
    {
        FileOutputStream fstream = null;
        try
        {
            fstream = new FileOutputStream(path, append);
            fstream.write(what.getBytes(Charset.forName("UTF-8")));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if (fstream != null) try
            {
                fstream.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
