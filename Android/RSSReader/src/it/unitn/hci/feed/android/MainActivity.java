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
import it.unitn.hci.feed.android.utils.DialogUtils;
import it.unitn.hci.feed.android.utils.RSSAsyncReader;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Course.CourseName;
import it.unitn.hci.feed.common.models.Feed;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends FragmentActivity
{
    private ExpandableListAdapter mCoursesAdapter;
    private ExpandableListView mCoursesList;

    private Map<String, List<Feed>> mCourses;
    private ImageView btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);
        btnMenu = (ImageView) findViewById(R.id.btnMenu);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading feeds");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        RSSAsyncReader.getFeedsAsync(CourseName.ANALISI_MATEMATICA_III, new Action<TaskResult<List<Feed>>>()
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

        btnMenu.setOnClickListener(onBtnMenuClicked(this));
    }

    private final static OnClickListener mCourseClickedListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        }
    };


    private static OnClickListener onBtnMenuClicked(final Context context)
    {
        return new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogUtils.showPopupWindowMenu(context, v, mOnManageFeedsCkicked, mOnShowAllFeedsCkicked, mOnEnableNotificationCkicked);
            }
        };
    }

    private static final OnClickListener mOnManageFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

        }
    };

    private static final OnClickListener mOnShowAllFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

        }
    };

    private static final OnClickListener mOnEnableNotificationCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

        }
    };

}
