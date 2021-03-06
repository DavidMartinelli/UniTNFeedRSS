package it.unitn.hci.feed.android.adapter;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.models.Department;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DepartmentAdapter extends ArrayAdapter<Department>
{
    private List<Department> mDepartments;
    private Context mContext;
    private LayoutInflater mInflater;


    public DepartmentAdapter(Context context, List<Department> departments)
    {
        super(context, R.layout.department_chooser_item_layout, departments);
        mDepartments = departments;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.department_chooser_item_layout, null);
        TextView departmentName = (TextView) convertView.findViewById(R.id.lblDepartmentName);

        departmentName.setText(mDepartments.get(position).getName());

        return convertView;
    }

}
