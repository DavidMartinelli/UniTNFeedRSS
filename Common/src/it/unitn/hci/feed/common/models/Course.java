package it.unitn.hci.feed.common.models;

import java.util.Random;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course implements Model
{
    public static final String GENERIC_COURSE_NAME = "GENERIC";
    private static final int DEFAUL_COLOUR = 000000;

    @XmlElement(name = "id")
    private int mId;

    @XmlElement(name = "name")
    private String mName;

    @XmlElement(name = "colour")
    private int mColour;


    public Course(int id, String name, int colour)
    {
        super();
        this.mId = id;
        this.mName = name;
        this.mColour = colour;
    }


    public int getId()
    {
        return mId;
    }


    public String getName()
    {
        return mName;
    }


    public String getStringName()
    {
        return mName.toString().replace("_", " ").toLowerCase();
    }


    public int getColour()
    {
        return mColour;
    }


    @Override
    public boolean isPersistent()
    {
        return mId >= 0;
    }


    public static Course notCached(String subject, int colour)
    {
        return new Course(-1, subject, colour);
    }


    public static Course notCached(String subject)
    {
        return new Course(-1, subject, DEFAUL_COLOUR);
    }


    public static int generateRandomColor()
    {
        Random rand = new Random();
        return rand.nextInt();
    }
}
