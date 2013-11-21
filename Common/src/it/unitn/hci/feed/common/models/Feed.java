package it.unitn.hci.feed.common.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feed implements Model
{
    @XmlElement(name = "body")
    private String mBody;

    @XmlElement(name = "id")
    private int mId;

    @XmlElement(name = "timeStamp")
    private long mTimeStamp;

    @XmlElement(name = "course")
    private Course mCourse;


    Feed()
    {
    };


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


    public Course getCourse()
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
        builder.append("\tSubject: " + mCourse.getStringName() + "\n");
        builder.append("\tID: " + mId + "\n");
        builder.append("\tTimeStamp: " + mTimeStamp + "\n");
        builder.append("\tBody: " + mBody);
        builder.append("\n]");
        return builder.toString();
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mBody == null) ? 0 : mBody.hashCode());
        result = prime * result + ((mCourse == null) ? 0 : mCourse.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Feed other = (Feed) obj;
        if (mBody == null)
        {
            if (other.mBody != null) return false;
        }
        else if (!mBody.equals(other.mBody)) return false;
        if (mCourse == null)
        {
            if (other.mCourse != null) return false;
        }
        else if (!mCourse.equals(other.mCourse)) return false;
        return true;
    }

}
