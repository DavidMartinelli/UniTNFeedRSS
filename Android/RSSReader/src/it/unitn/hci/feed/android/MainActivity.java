package it.unitn.hci.feed.android;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.RssService.LocalBinder;
import it.unitn.hci.feed.android.adapter.FeedsAdapter;
import it.unitn.hci.feed.android.models.Course;
import it.unitn.hci.feed.android.models.Department;
import it.unitn.hci.feed.android.models.Feed;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.android.utils.DialogUtils;
import it.unitn.hci.feed.android.utils.RefreshTask;
import it.unitn.hci.feed.android.utils.SharedUtils;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;

public class MainActivity extends FragmentActivity
{
    public static final String DIALOG_INTENT = "dialog";

    private FeedsAdapter mCoursesAdapter;
    private ExpandableListView mCoursesList;

    private Map<String, List<Feed>> mCourses = new HashMap<String, List<Feed>>();
    private ImageView btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);
        btnMenu = (ImageView) findViewById(R.id.btnMenu);

        mCoursesList.setOnChildClickListener(onFeedClickListener);
        btnMenu.setOnClickListener(onBtnMenuClicked(this));

        loadAndShowFeeds();

        Intent mIntent = new Intent(this, RssService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        
        IntentFilter intentFilter = new IntentFilter(RefreshTask.REFRESH_DATA_INTENT);
        registerReceiver(mIntentReceiver, intentFilter);
    }

    
    private void loadAndShowFeeds() {
        try
        {
            DatabaseManager manager = DatabaseManager.instantiate(this);
            List<Feed> feeds = manager.getFeeds();
            List<String> orderedKeys = displayFeeds(feeds);
            mCoursesAdapter = new FeedsAdapter(MainActivity.this, mCourses, orderedKeys);
            mCoursesList.setAdapter(mCoursesAdapter);
            DatabaseManager.close(manager);
        }
        catch (Exception e)
        {
            // non ce ne saranno
            e.printStackTrace();
        }
    }
    
    @SuppressLint("SimpleDateFormat")
    protected List<String> displayFeeds(List<Feed> feeds)
    {
        final SimpleDateFormat dateFormater = new SimpleDateFormat("MMMMM dd, yyyy");

        List<Feed> savedFeeds;
        List<Timestamp> orderedTimestamp = new ArrayList<Timestamp>();
        for (Feed f : feeds)
        {
            Timestamp t = new Timestamp(f.getTimeStamp());
            String timeStamp = dateFormater.format(t).toUpperCase();
            savedFeeds = mCourses.get(timeStamp);
            if (savedFeeds != null)
            {
                savedFeeds.add(f);
            }
            else
            {
                savedFeeds = new ArrayList<Feed>();
                savedFeeds.add(f);
                orderedTimestamp.add(t);
                mCourses.put(timeStamp, savedFeeds);
            }

        }
        Collections.reverse(orderedTimestamp);

        List<String> orderedDates = new ArrayList<String>();

        for (Timestamp tstamp : orderedTimestamp)
        {
            orderedDates.add(dateFormater.format(tstamp).toUpperCase());
        }
        return orderedDates;
    }


    private OnClickListener onBtnMenuClicked(final Context context)
    {
        return new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    DialogUtils.showPopupWindowMenu(context, v, mOnManageFeedsCkicked, mOnShowAllFeedsCkicked, mOnEnableNotificationCkicked);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, context.getString(R.string.an_error_has_occurred_saving_your_preference), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private final OnClickListener mOnManageFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            manageFeeds();
        }
    };

    private final OnClickListener mOnShowAllFeedsCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            UnitnApi.getAllFeedsAsync(new Action<CallbackAsyncTask.TaskResult<List<Feed>>>()
            {
                @Override
                public void invoke(TaskResult<List<Feed>> param)
                {
                    if (param.exception != null)
                    ;// cè stato un errore, mostrare messaggio

                    DialogUtils.showAllFeeds(MainActivity.this, param.result);
                }
            });
        }
    };

    private final OnClickListener mOnEnableNotificationCkicked = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            try
            {
                SharedUtils.toogleNotificationPreference(MainActivity.this);

                final Context context = MainActivity.this;
                boolean isEnabled = SharedUtils.isNotificationsEnabled(context);
                if (isEnabled) Toast.makeText(context, context.getString(R.string.notification_enabled), Toast.LENGTH_LONG).show();
                else Toast.makeText(context, context.getString(R.string.notification_disabled), Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            { // TODO pensare a cosa fare
            }
        }
    };

    private final OnChildClickListener onFeedClickListener = new OnChildClickListener()
    {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
        {
            List<String> mDates = new ArrayList<String>(mCourses.keySet());
            DialogUtils.showFeed(MainActivity.this, mCourses.get(mDates.get(groupPosition)).get(childPosition));
            return true;
        }
    };


    private void manageFeeds()
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
                                        System.out.println(courses);
                                        SharedUtils.saveCourses(courses, MainActivity.this);
                                        try
                                        {
                                            DatabaseManager.instantiate(MainActivity.this).syncCourses(courses);
                                            DialogUtils.show(getString(R.string.saved_preferences), null, MainActivity.this, true, null, getString(R.string.ok), null);
                                            
                                            System.out.println("service");
                                            if (mService != null)
                                            {
                                                System.out.println("launch");
                                                Thread polling = mService.getPollingThread();
                                                synchronized (polling)
                                                {
                                                   polling.interrupt(); 
                                                }
                                            }
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

    private RssService mService;
    private final ServiceConnection mConnection = new ServiceConnection()
    {

        public void onServiceDisconnected(ComponentName name)
        {
            mService = null;
            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_LONG).show();
        }


        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mService = ((LocalBinder) service).getServerInstance();  
            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_LONG).show();
        }
    };

    BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals(RefreshTask.REFRESH_DATA_INTENT))
            {
                loadAndShowFeeds();
            }
        }
    };


    protected void onDestroy()
    {
        unregisterReceiver(mIntentReceiver);
    };
}
