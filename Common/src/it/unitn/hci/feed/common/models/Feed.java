package it.unitn.hci.feed.common.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@XmlRootElement
@DatabaseTable
public class Feed implements Model, Comparable<Feed>
{

    @XmlElement(name = "id")
    @DatabaseField(generatedId = true)
    private int mId;

    @XmlElement(name = "body")
    @DatabaseField
    private String mBody;

    @XmlElement(name = "timestamp")
    @DatabaseField
    private long mTimeStamp;

    @XmlElement(name = "course")
    @DatabaseField(foreign = true)
    private Course mCourse;


    Feed()
    {
        // for the orm
    }


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


    public void setCourse(Course course)
    {
        mCourse = course;
    }


    @Override
    public int compareTo(Feed o)
    {
        return Long.valueOf(o.mTimeStamp).compareTo(mTimeStamp);
    }
}
