package it.unitn.hci.feed.android;

import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.adapter.CoursesAdapter;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.android.utils.RSSAsyncReader;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;

public class MainActivity extends Activity
{
    private ExpandableListAdapter mCoursesAdapter;
    private ExpandableListView mCoursesList;

    private Map<String, List<Feed>> mCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading feeds");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        RSSAsyncReader.getFeedsAsync(Course.ANALISI_MATEMATICA_III, new Action<TaskResult<List<Feed>>>()
        {
            @Override
            public void invoke(TaskResult<List<Feed>> result)
            {
                dialog.dismiss();
                Exception exception = result.exception;
                if (exception != null)
                {
                    Toast.makeText(MainActivity.this, "Something went wrong: " + exception + ", " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                mCourses = new Hashtable<String, List<Feed>>();
                for (Feed f : result.result)
                {
                    final String timestamp = new Date(f.getTimeStamp()).toString();
                    List<Feed> feeds = mCourses.get(timestamp);
                    if (feeds == null) feeds = new LinkedList<Feed>();
                    feeds.add(f);
                    mCourses.put(timestamp, feeds);
                }

                mCoursesAdapter = new CoursesAdapter(MainActivity.this, mCourses, mCourseClickedListener);
                mCoursesList.setAdapter(mCoursesAdapter);
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

}
