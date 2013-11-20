package it.unitn.hci.utils;

public class ColourUtils
{

    public static int getColorDifference(int color1, int color2)
    {
        int red1 = Color.red(color1);
        int green1 = Color.green(color1);
        int blue1 = Color.blue(color1);

        int red2 = Color.red(color2);
        int green2 = Color.green(color2);
        int blue2 = Color.blue(color2);
        return (Math.max(red1, red2) - Math.min(red1, red2)) + (Math.max(green1, green2) - Math.min(green1, green2)) + (Math.max(blue1, blue2) - Math.min(blue1, blue2));
    }
}
