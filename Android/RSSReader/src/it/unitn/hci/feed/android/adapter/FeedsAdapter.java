package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.common.models.Feed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedsAdapter extends BaseExpandableListAdapter
{

    private List<String> mDates;
    private Map<String, List<Feed>> mCourses;
    private Context mContext;
    private LayoutInflater mInflater;


    public FeedsAdapter(Context context, Map<String, List<Feed>> courses)
    {
        mCourses = courses;
        mContext = context;
        List<String> l = new ArrayList<String>(courses.keySet());
        mDates = l;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mCourses.get(getGroup(groupPosition)).get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.feed_content_layout, null);
        TextView lblCourseName = (TextView) convertView.findViewById(R.id.lblCourseName);
        TextView lblCourseDescription = (TextView) convertView.findViewById(R.id.lblCourseDescription);
        ImageView imgCourseColor = (ImageView) convertView.findViewById(R.id.imgCoursesColor);

        Feed feed = (Feed) getChild(groupPosition, childPosition);

        lblCourseName.setText(feed.getSubject().getStringName());
        lblCourseDescription.setText(feed.getBody());
        imgCourseColor.setBackgroundColor(feed.getSubject().getColour());
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mCourses.get(getGroup(groupPosition)).size();
    }


    @Override
    public Object getGroup(int groupPosition)
    {
        return mDates.get(groupPosition);
    }


    @Override
    public int getGroupCount()
    {
        return mDates.size();
    }


    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.feed_header_layout, null);

        String headerTitle = (String) getGroup(groupPosition);
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListItem);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }


    @Override
    public boolean hasStableIds()
    {
        return false;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

}
