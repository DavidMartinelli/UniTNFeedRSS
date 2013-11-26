package it.unitn.hci.feed.common.models;

import java.util.Set;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Department
{

    @XmlElement(name = "name")
    private String mName;

    @XmlElement(name = "id")
    private int mId;

    private String mCSSSelector;
    private String mBulletinNewsUrl;

    private Set<Course> mCourses;


    Department()
    {
    }


    public Department(String name, String CSSSelector, String bulletinNewsURL, Set<Course> courses)
    {
        mName = name;
        mCSSSelector = CSSSelector;
        mBulletinNewsUrl = bulletinNewsURL;
        mCourses = courses;
        for (Course c : mCourses)
        {
            c.setDepartment(this);
        }
    }


    public Set<Course> getCourses()
    {
        return mCourses;
    }


    public String getName()
    {
        return mName;
    }


    public int getId()
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
}
