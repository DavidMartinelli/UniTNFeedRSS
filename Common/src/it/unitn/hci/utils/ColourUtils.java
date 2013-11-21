package it.unitn.hci.utils;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;

public class ColourUtils
{

    public static boolean areInContrast(int colour, HashSet<Integer> colours)
    {
        Iterator<Integer> iterator = colours.iterator();
        while (iterator.hasNext())
        {
            if (getColorDifference(colour, iterator.next()) < 20) return false;
        }
        return true;
    }


    public static int getColorDifference(int color1, int color2)
    {
        Color color = new Color(color1);
        int red1 = color.getRed();
        int green1 = color.getGreen();
        int blue1 = color.getBlue();

        color = new Color(color2);
        int red2 = color.getRed();
        int green2 = color.getGreen();
        int blue2 = color.getBlue();
        return (Math.max(red1, red2) - Math.min(red1, red2)) + (Math.max(green1, green2) - Math.min(green1, green2)) + (Math.max(blue1, blue2) - Math.min(blue1, blue2));
    }
}
