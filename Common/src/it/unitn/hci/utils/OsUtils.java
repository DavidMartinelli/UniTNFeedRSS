package it.unitn.hci.utils;

public class OsUtils
{
    private OsUtils()
    {
        // static methods only
    }


    public static void vaFateNaDormida(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            // ignore
        }
    }
}
