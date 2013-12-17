package it.unitn.hci.feed.android.utils;

public class StringUtils
{
    private StringUtils()
    {
        //Static method only
    }
    
    public static String capitalize(String string)
    {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
