package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.common.models.Course;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<Course>
{
    private List<Course> mCourses;
    private Context mContext;
    private LayoutInflater mInflater;


    public CourseAdapter(Context context, List<Course> courses)
    {
        super(context, R.layout.courses_chooser_layout, courses);
        mCourses = courses;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.courses_chooser_item_layout, null);
        TextView departmentName = (TextView) convertView.findViewById(R.id.lblCourseName);

        departmentName.setText(mCourses.get(position).getName());

        return convertView;
    }

}
