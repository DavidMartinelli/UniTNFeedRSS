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

    @XmlElement(name = "timestamp")
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
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Feed)) return false;

        Feed other = (Feed) obj;
        if ((mBody == null && other.mBody != null) || !mBody.equals(other.mBody)) return false;

        if (mCourse == null && other.mCourse == null) return true;
        return mCourse != null && mCourse.equals(other.mCourse);
    }

}
