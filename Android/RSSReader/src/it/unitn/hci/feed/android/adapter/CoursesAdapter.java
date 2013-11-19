package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoursesAdapter extends BaseExpandableListAdapter
{

    private OnClickListener mListener;
    private HashMap<String, List<Pair<String, String>>> mCourses;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> mDates;


    public CoursesAdapter(Context context, List<String> dates, HashMap<String, List<Pair<String, String>>> courses, OnClickListener listener)
    {
        mListener = listener;
        mCourses = courses;
        mContext = context;
        mDates = dates;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mCourses.get(mDates.get(groupPosition)).get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }


    @SuppressWarnings("unchecked")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.feed_content_layout, null);
        TextView lblCourseName = (TextView) convertView.findViewById(R.id.lblCourseName);
        TextView lblCourseDescription = (TextView) convertView.findViewById(R.id.lblCourseDescription);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.lyCourse);
        Pair<String, String> course = (Pair<String, String>) getChild(groupPosition, childPosition);

        lblCourseName.setText(course.first);
        lblCourseDescription.setText(course.second);
        layout.setOnClickListener(mListener);

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mCourses.get(mDates.get(groupPosition)).size();
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
