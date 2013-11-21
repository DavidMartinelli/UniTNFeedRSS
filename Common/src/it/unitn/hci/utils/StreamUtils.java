package it.unitn.hci.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StreamUtils
{
    private StreamUtils()
    {
        // static methods only
    }


    public static List<String> readLines(InputStream stream) throws Exception
    {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
            return lines;
        }
        finally
        {
            if (reader != null) reader.close();
        }
    }


    public static String readAll(InputStream stream) throws Exception
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder b = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                b.append(line);
            }
            return b.toString();
        }
        finally
        {
            if (reader != null) reader.close();
        }
    }
}
