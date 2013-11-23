package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.common.models.Course;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<Course> implements OnItemClickListener
{
    private List<Course> mCourses;
    private List<Boolean> mSelected;
    private Context mContext;
    private LayoutInflater mInflater;


    public CourseAdapter(Context context, List<Course> courses)
    {
        super(context, R.layout.courses_chooser_layout, courses);
        mCourses = courses;
        mContext = context;
        mSelected = new ArrayList<Boolean>(mCourses.size());
        for (int i = 0; i < mCourses.size(); i++)
            mSelected.set(i, false);
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


    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
    {
        mSelected.set(position, !mSelected.get(position));
    }

}
