package it.unitn.hci.feed.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.adapter.CoursesAdapter;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.android.utils.RSSAsyncReader;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity
{
    private ExpandableListAdapter mCoursesAdapter;
    private ExpandableListView mCoursesList;
    private Context mContext;

    List<String> mHeaders;
    HashMap<String, List<Pair<String, String>>> mCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);
        mContext = this;

        prepareListData();

        mCoursesAdapter = new CoursesAdapter(mContext, mHeaders, mCourses, mCourseClickedListener);
        mCoursesList.setAdapter(mCoursesAdapter);

        RSSAsyncReader.getFeedsAsync(Course.ANALISI_MATEMATICA_III, new Action<TaskResult<List<Feed>>>()
        {
            @Override
            public void invoke(TaskResult<List<Feed>> param)
            {
                Toast.makeText(MainActivity.this, "Rss readed async, result: " + param.result, Toast.LENGTH_LONG).show();
            }
        });
    }

    private final static OnClickListener mCourseClickedListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        }
    };


    private void prepareListData()
    {
        mHeaders = new ArrayList<String>();
        mCourses = new HashMap<String, List<Pair<String, String>>>();

        // Adding child data
        mHeaders.add("21/11/2013");

        // Adding child data
        List<Pair<String, String>> c = new ArrayList<Pair<String, String>>();
        c.add(new Pair<String, String>("HCI", "prova1"));
        c.add(new Pair<String, String>("ISDE", "prova2"));

        mCourses.put(mHeaders.get(0), c);
    }
}
