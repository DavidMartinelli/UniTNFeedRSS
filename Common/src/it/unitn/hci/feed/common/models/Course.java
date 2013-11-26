package it.unitn.hci.feed.common.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course implements Model
{
    public static final String GENERIC_COURSE_NAME = "GENERIC";
    public static final Course GENERIC_COURSE = new Course(1, GENERIC_COURSE_NAME, generateRandomColor(), new HashSet<String>(Arrays.asList(GENERIC_COURSE_NAME)));
    private static final int DEFAUL_COLOUR = 000000;

    @XmlElement(name = "id")
    private int mId;

    @XmlElement(name = "name")
    private String mName;

    @XmlElement(name = "colour")
    private int mColour;

    private Set<String> mAliases;

    private Department mDepartment;


    public void setDepartment(Department department)
    {
        mDepartment = department;
    }


    Course()
    {
    }


    public Course(String name)
    {
        mName = name;
    }


    public Course(int id, String name, int colour, Set<String> aliases)
    {
        super();
        mId = id;
        mName = name;
        mColour = colour;
        mAliases = aliases;
    }


    public Course(int id, String name, int colour, Set<String> aliases, Department department)
    {
        super();
        mId = id;
        mName = name;
        mColour = colour;
        mAliases = aliases;
        mDepartment = department;
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


    public Set<String> getAliases()
    {
        return mAliases;
    }


    public Department getDepartment()
    {
        return mDepartment;
    }


    @Override
    public boolean isPersistent()
    {
        return mId >= 0;
    }


    public static Course notCached(String subject, int colour)
    {
        return new Course(-1, subject, colour, null);
    }


    public static Course notCached(String subject)
    {
        return new Course(-1, subject, DEFAUL_COLOUR, null);
    }


    public boolean isGeneric()
    {
        return mId == 0;
    }


    public static int generateRandomColor()
    {
        Random rand = new Random();
        return rand.nextInt();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Course)) return false;
        if (obj == this) return true;
        Course casted = (Course) obj;
        return casted.getName().equals(getName()) && casted.getId() == getId();
    }


    @Override
    public String toString()
    {
        return "id: " + mId + ", name: " + mName + ", aliases: " + mAliases + ", colour: " + mColour + ", department: " + mDepartment.getName();
    }

}
