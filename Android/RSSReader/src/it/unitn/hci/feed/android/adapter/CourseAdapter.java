package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.DatabaseManager;
import it.unitn.hci.feed.android.models.Course;
import it.unitn.hci.feed.android.utils.StringUtils;
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

public class CourseAdapter extends ArrayAdapter<Course>
{
    private List<Course> mCourses;
    private List<Boolean> mSelected;
    private Context mContext;
    private LayoutInflater mInflater;


    public CourseAdapter(Context context, List<Course> courses) throws Exception
    {
        super(context, R.layout.courses_chooser_layout, courses);
        mCourses = courses;
        mContext = context;

        if (mCourses == null) return;
        mSelected = new ArrayList<Boolean>();
        List<Course> savedCourses = DatabaseManager.instantiate(context).getCourses();
        for (Course c:courses){
            mSelected.add(savedCourses.contains(c));
        }
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.courses_chooser_item_layout, null);
        View pnlCourse = (View) convertView.findViewById(R.id.pnlCourse);
        TextView departmentName = (TextView) convertView.findViewById(R.id.lblCourseName);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.cbSelectCourse);
        pnlCourse.setOnClickListener(setChecked(position, cb));
        cb.setOnClickListener(setChecked(position, cb));
        cb.setChecked(mSelected.get(position));

        departmentName.setText(StringUtils.capitalize(mCourses.get(position).getName()));

        return convertView;
    }


    public List<Boolean> getSelected()
    {
        return mSelected;
    }

    public OnClickListener setChecked(final int position, final CheckBox cb)
    {
        return new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mSelected.set(position, !mSelected.get(position));
                cb.setChecked(mSelected.get(position));
            }
        };
    }

}
