package it.unitn.hci.feed.android.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Alias
{
    @DatabaseField(generatedId = true)
    private int mId;

    @DatabaseField(uniqueCombo = true)
    private String mValue;

    @DatabaseField(foreign = true, uniqueCombo = true)
    private Course mCourse;


    public Alias(String value, Course course)
    {
        this.mValue = value;
        this.mCourse = course;
    }


    public Alias()
    {
        // for the orm
    }


    public String getValue()
    {
        return mValue;
    }


    public Course getCourse()
    {
        return mCourse;
    }


    public void setCourse(Course course)
    {
        mCourse = course;
    }

}
