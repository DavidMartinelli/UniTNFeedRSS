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

    private String mCSSSelector;
    private String mLink;


    Department()
    {
    }


    public Department(String name, String CSSSelector, String link)
    {
        mName = name;
        mCSSSelector = CSSSelector;
        mLink = link;
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


    public String getLink()
    {
        return mLink;
    }
    
    @Override
    public String toString()
    {
        return mName;
    }
}
