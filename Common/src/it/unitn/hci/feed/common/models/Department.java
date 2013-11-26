package it.unitn.hci.feed.common.models;

import java.util.Collection;
import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@XmlRootElement
@DatabaseTable
public class Department
{

    @DatabaseField(generatedId = true)
    @XmlElement(name = "id")
    private long mId;

    @DatabaseField(unique = true)
    @XmlElement(name = "name")
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
