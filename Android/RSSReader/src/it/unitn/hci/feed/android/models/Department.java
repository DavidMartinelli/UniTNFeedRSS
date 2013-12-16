package it.unitn.hci.feed.android.models;

import java.util.Collection;
import java.util.Set;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class Department
{

    @DatabaseField(generatedId = true)
    private long mId;

    @DatabaseField(unique = true)
    private String mName;

    @DatabaseField
    private String mCSSSelector;

    @DatabaseField
    private String mBulletinNewsUrl;

    @ForeignCollectionField(eager = true)
    private Collection<Course> mCourses;


    Department()
    {
        // for the orm
    }


    public Department(String name, String CSSSelector, String bulletinNewsURL, Set<Course> courses)
    {
        mName = name;
        mCSSSelector = CSSSelector;
        mBulletinNewsUrl = bulletinNewsURL;
        mCourses = courses;
        if (mCourses == null) return;
        for (Course c : mCourses)
        {
            c.setDepartment(this);
        }
    }


    public Collection<Course> getCourses()
    {
        return mCourses;
    }


    public String getName()
    {
        return mName;
    }


    public long getId()
    {
        return mId;
    }


    public String getCSSSelector()
    {
        return mCSSSelector;
    }


    public String getBulletinNewsURL()
    {
        return mBulletinNewsUrl;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(mName + "\n");
        for (Course course : mCourses)
            builder.append("\t" + course + "\n");
        return builder.toString();
    }


    public void setId(long id)
    {
        mId = id;
    }
}
