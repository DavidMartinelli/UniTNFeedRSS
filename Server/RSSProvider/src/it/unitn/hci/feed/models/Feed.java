package it.unitn.hci.feed.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feed implements Model
{
    @XmlElement(name="body")
    private String mBody;
    
    @XmlElement(name="id")
    private int mId;
    
    @XmlElement(name="timeStamp")
    private long mTimeStamp;
    
    @XmlElement(name="course")
    private Course mCourse;


    public Feed(int id, String body, long timeStamp, Course course)
    {
        mId = id;
        mBody = body;
        mTimeStamp = timeStamp;
        mCourse = course;
    }


    public String getBody()
    {
        return mBody;
    }


    public int getId()
    {
        return mId;
    }


    public long getTimeStamp()
    {
        return mTimeStamp;
    }


    public Course getSubject()
    {
        return mCourse;
    }


    @Override
    public boolean isPersistent()
    {
        return mId >= 0;
    }


    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder("[\n");
        builder.append("\tSubject: " + mCourse + "\n");
        builder.append("\tID: " + mId + "\n");
        builder.append("\tTimeStamp: " + mTimeStamp + "\n");
        builder.append("\tBody: " + mBody + "\n");
        builder.append("]");
        return builder.toString();
    }

}
