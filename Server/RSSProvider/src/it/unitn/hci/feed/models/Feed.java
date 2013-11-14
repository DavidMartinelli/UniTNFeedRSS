package it.unitn.hci.feed.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Feed implements Model
{
    private String mBody;
    private int mId;
    private long mTimeStamp;
    private Subject mSubject;


    public Feed(int id, String body, long timeStamp, Subject subject)
    {
        mId = id;
        mBody = body;
        mTimeStamp = timeStamp;
        mSubject = subject;
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


    public Subject getSubject()
    {
        return mSubject;
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
        builder.append("\tSubject: " + mSubject + "\n");
        builder.append("\tID: " + mId + "\n");
        builder.append("\tTimeStamp: " + mTimeStamp + "\n");
        builder.append("\tBody: " + mBody + "\n");
        builder.append("]");
        return builder.toString();
    }

}
