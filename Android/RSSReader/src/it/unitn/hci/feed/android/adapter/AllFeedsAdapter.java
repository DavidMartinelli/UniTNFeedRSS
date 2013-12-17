package it.unitn.hci.feed.android.adapter;

import java.util.List;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.models.Feed;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AllFeedsAdapter extends ArrayAdapter<Feed>
{

    private List<Feed> mFeeds;
    private LayoutInflater mInflater;


    public AllFeedsAdapter(Context context, List<Feed> objects)
    {
        super(context, R.layout.feed_content_layout, objects);
        mFeeds = objects;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) convertView = mInflater.inflate(R.layout.feed_content_layout, null);
        Feed f = mFeeds.get(position);

        TextView lblTitle = (TextView) convertView.findViewById(R.id.lblCourseName);
        if (f.getCourse() != null) lblTitle.setText(f.getCourse().getName());

        TextView lblBody = (TextView) convertView.findViewById(R.id.lblCourseDescription);
        lblBody.setText(f.getBody());

        return convertView;
    }
}
