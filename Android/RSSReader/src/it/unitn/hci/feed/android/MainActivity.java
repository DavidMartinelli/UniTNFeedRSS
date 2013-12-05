package it.unitn.hci.feed.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.adapter.FeedsAdapter;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.android.utils.DialogUtils;
import it.unitn.hci.feed.android.utils.SharedUtils;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

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
        setContentView(R.layout.main_activity);

        mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);
        btnMenu = (ImageView) findViewById(R.id.btnMenu);

        // final ProgressDialog dialog = new ProgressDialog(this);
        // dialog.setTitle("Loading feeds");
        // dialog.setMessage("Please wait...");
        // dialog.setCancelable(false);
        // dialog.show();
        //
        // TODO prendere da db i feeds e ordinarli per data
        // UnitnApi.getFeedsAsync("ANALISI_MATEMATICA_III", new Action<TaskResult<List<Feed>>>()
        // {
        // @Override
        // public void invoke(TaskResult<List<Feed>> result)
        // {
        // dialog.dismiss();
        // Exception exception = result.exception;
        // if (exception != null)
        // {
        // Toast.makeText(MainActivity.this, "Something went wrong: " + exception + ", " + exception.getMessage(), Toast.LENGTH_LONG).show();
        // exception.printStackTrace();
        // return;
        // }
        //
        // mCourses = new Hashtable<String, List<Feed>>();
        // for (Feed f : result.result)
        // {
        // final String timestamp = new Date(f.getTimeStamp()).toString();
        // List<Feed> feeds = mCourses.get(timestamp);
        // if (feeds == null) feeds = new LinkedList<Feed>();
        // feeds.add(f);
        // mCourses.put(timestamp, feeds);
        // }
        //
        // mCoursesAdapter = new FeedsAdapter(MainActivity.this, mCourses);
        // mCoursesList.setAdapter(mCoursesAdapter);
        // mCoursesList.setOnChildClickListener(new OnChildClickListener()
        // {
        // @Override
        // public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
        // {
        // List<String> mDates = new ArrayList<String>(mCourses.keySet());
        // DialogUtils.showFeed(MainActivity.this, mCourses.get(mDates.get(groupPosition)).get(childPosition));
        // return true;
        // }
        // });
        // }
        // });

        btnMenu.setOnClickListener(onBtnMenuClicked(this));
    }


    private OnClickListener onBtnMenuClicked(final Context context)
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

    private final OnClickListener mOnManageFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            final ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getString(R.string.loadings_departments));
            pDialog.show();

            UnitnApi.getDepartmentsAsync(new Action<CallbackAsyncTask.TaskResult<List<Department>>>()
            {
                @Override
                public void invoke(TaskResult<List<Department>> param)
                {
                    pDialog.dismiss();
                    final List<Department> result = param.result;
                    if (param.exception != null || result.isEmpty())
                    {
                        param.exception.printStackTrace();
                        DialogUtils.show(getString(R.string.an_error_has_occurred_loadings_departments), null, MainActivity.this, true, null, getString(R.string.ok), null);
                        return;
                    }

                    DialogUtils.showDepartmentsList(MainActivity.this, result, new OnItemClickListener()
                    {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
                        {
                            Department dep = result.get(position);
                            UnitnApi.getCoursesAsync(dep, new Action<CallbackAsyncTask.TaskResult<List<Course>>>()
                            {
                                @Override
                                public void invoke(TaskResult<List<Course>> param)
                                {
                                    if (param.exception != null) DialogUtils.show(getString(R.string.an_error_has_occurred_loadings_courses), null, MainActivity.this, true, null, getString(R.string.ok), null);

                                    DialogUtils.showCoursesSelector(MainActivity.this, param.result, new Action<List<Course>>()
                                    {
                                        @Override
                                        public void invoke(List<Course> courses)
                                        {
                                            SharedUtils.saveCourses(courses, MainActivity.this);
                                            try
                                            {
                                                DatabaseManager.instantiate(MainActivity.this).syncCourses(courses);
                                            }
                                            catch (Exception e)
                                            {
                                                DialogUtils.show(getString(R.string.an_error_has_occurred_saving_your_subscription), null, MainActivity.this, true, null, getString(R.string.ok), null);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    };

    private final OnClickListener mOnShowAllFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

        }
    };

    private final OnClickListener mOnEnableNotificationCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

        }
    };

}
