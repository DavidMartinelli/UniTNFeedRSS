package it.unitn.hci.feed.common.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Department
{

    @XmlElement(name = "name")
    private String mName;

    @XmlElement(name = "id")
    private int mId;


    Department()
    {
    }


    public Department(String name)
    {
        mName = name;
    }


    public String getName()
    {
        return mName;
    }


    public int getId()
    {
        return mId;
    }
}
