package it.unitn.hci.feed.android.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import android.annotation.SuppressLint;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Course implements Model
{
    public static final String GENERIC_COURSE_NAME = "GENERIC";
    public static final Course GENERIC_COURSE = new Course(1, GENERIC_COURSE_NAME, generateRandomColor(), new HashSet<Alias>(Arrays.asList(new Alias(GENERIC_COURSE_NAME, null))));
    private static final int DEFAUL_COLOUR = 000000;

    @DatabaseField(generatedId = true)
    private Integer mId;

    @DatabaseField(uniqueCombo = true)
    private String mName;

    @DatabaseField
    private int mColour;

    @ForeignCollectionField(eager = true)
    private Collection<Alias> mAliases;

    @ForeignCollectionField(eager = true)
    private Collection<Feed> mFeeds;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private Department mDepartment;


    @SuppressLint("DefaultLocale")
    public String getDisplayName()
    {
        return mName.replace('_', ' ').toLowerCase();
    }


    public void setDepartment(Department department)
    {
        mDepartment = department;
    }


    Course()
    {
        // for the orm
    }


    public Course(String name)
    {
        mName = name;
    }


    public Course(Integer id, String name, int colour, Set<Alias> aliases)
    {
        super();
        mId = id;
        mName = name;
        mColour = colour;
        mAliases = aliases;
        mFeeds = new HashSet<Feed>();
    }


    public Course(Integer id, String name, int colour, Set<Alias> aliases, Department department)
    {
        this(id, name, colour, aliases);
        mDepartment = department;
    }


    public void addFeed(Feed f)
    {
        f.setCourse(this);
        mFeeds.add(f);
    }


    public int getId()
    {
        return mId;
    }


    public String getName()
    {
        return mName;
    }


    public Collection<Feed> getFeeds()
    {
        return mFeeds;
    }


    public String getStringName()
    {
        return mName.toString().replace("_", " ").toLowerCase();
    }


    public int getColour()
    {
        return mColour;
    }


    public Collection<Alias> getAliases()
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
        return mId != null;
    }


    public static Course notCached(String subject, int colour)
    {
        return new Course(null, subject, colour, null);
    }


    public static Course notCached(String subject)
    {
        return new Course(null, subject, DEFAUL_COLOUR, null);
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
        return "id: " + mId + ", name: " + mName + ", aliases: " + mAliases + ", colour: " + mColour + ", department: " + (mDepartment == null ? null : mDepartment.getName());
    }


    public void setId(long id)
    {
        mId = (int) id;
    }

}
