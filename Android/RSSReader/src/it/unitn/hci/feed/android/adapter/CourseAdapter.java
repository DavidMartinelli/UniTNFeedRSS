package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.models.Course;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CourseAdapter extends ArrayAdapter<Course> implements OnClickListener
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

        if (mCourses == null) return;
        mSelected = new ArrayList<Boolean>();
        for (int i = 0; i < mCourses.size(); i++)
            mSelected.add(i, false);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.courses_chooser_item_layout, null);
        TextView departmentName = (TextView) convertView.findViewById(R.id.lblCourseName);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.cbSelectCourse);
        cb.setOnClickListener(this);
        cb.setTag(position);

        departmentName.setText(mCourses.get(position).getName());

        return convertView;
    }


    public List<Boolean> getSelected()
    {
        return mSelected;
    }


    @Override
    public void onClick(View view)
    {
        int position = (Integer) view.getTag();
        mSelected.set(position, !mSelected.get(position));
    }

}
