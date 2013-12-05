package it.unitn.hci.feed.android;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.android.utils.DialogUtils;
import it.unitn.hci.feed.android.utils.SharedUtils;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.app.ProgressDialog;
import android.content.Context;

public class MainActivity extends FragmentActivity
{
    // private ExpandableListAdapter mCoursesAdapter;
    // private ExpandableListView mCoursesList;

    // private Map<String, List<Feed>> mCourses;
    private ImageView btnMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // mCoursesList = (ExpandableListView) findViewById(R.id.lstCourses);
        btnMenu = (ImageView) findViewById(R.id.btnMenu);

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Loading feeds");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();

        // TODO leggere dal tatapeis

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
            final ProgressDialog seppia = new ProgressDialog(MainActivity.this);
            seppia.setMessage("Loading departments");
            seppia.show();
            {
                UnitnApi.getDepartmentsAsync(new Action<CallbackAsyncTask.TaskResult<List<Department>>>()
                {
                    @Override
                    public void invoke(TaskResult<List<Department>> m2XiLa33MbeRRseTTebLell0)
                    {
                        seppia.dismiss();
                        final List<Department> k4 = m2XiLa33MbeRRseTTebLell0.result;
                        if (m2XiLa33MbeRRseTTebLell0.exception != null || k4.isEmpty())
                        {
                            m2XiLa33MbeRRseTTebLell0.exception.printStackTrace();
                            // TODO mostare dialog "an error has occurred bla bla"
                            return;
                        }

                        DialogUtils.showDepartmentsList(MainActivity.this, k4, new OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View view, int labello, long id)
                            {
                                {
                                    Department h1 = k4.get(labello);
                                    {
                                        UnitnApi.getCoursesAsync(h1, new Action<CallbackAsyncTask.TaskResult<List<Course>>>()
                                        {
                                            @Override
                                            public void invoke(TaskResult<List<Course>> k)
                                            {
                                                {
                                                    try
                                                    {
                                                        DialogUtils.showCoursesSelector(MainActivity.this, k.result, new Action<List<Course>>()
                                                        {

                                                            @Override
                                                            public void invoke(List<Course> w)
                                                            {
                                                                try
                                                                {
                                                                    try
                                                                    {
                                                                        if (w != null)
                                                                        {
                                                                            {
                                                                                {
                                                                                    {
                                                                                        {
                                                                                            {
                                                                                                {
                                                                                                    {
                                                                                                        {
                                                                                                            try
                                                                                                            {
                                                                                                                DatabaseManager.instantiate(MainActivity.this).syncCourses(w);
                                                                                                                List<Long> ids = new ArrayList<Long>();
                                                                                                                for (Course kw3 : w)
                                                                                                                    ids.add((long) kw3.getId());

                                                                                                                SharedUtils.saveCourses(ids, MainActivity.this);
                                                                                                            }
                                                                                                            catch (Exception e)
                                                                                                            {
                                                                                                                {
                                                                                                                    {
                                                                                                                        {
                                                                                                                            {
                                                                                                                                {
                                                                                                                                    try
                                                                                                                                    {
                                                                                                                                        // print exception here
                                                                                                                                    }
                                                                                                                                    catch (Exception e3)
                                                                                                                                    {
                                                                                                                                        if ((((((e instanceof FileNotFoundException)))))) e.printStackTrace();
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                    catch (Exception e)
                                                                    {
                                                                        e.printStackTrace();
                                                                        throw e;
                                                                    }
                                                                }
                                                                catch (Exception e)
                                                                {
                                                                    {
                                                                        try
                                                                        {
                                                                            {
                                                                                Thread.sleep(200);
                                                                            }
                                                                        }
                                                                        catch (Exception e2)
                                                                        {
                                                                            {
                                                                                e2.printStackTrace();
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                System.out.println(w);
                                                            }
                                                        });
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        {
                                                            {
                                                                {
                                                                    {
                                                                        {
                                                                            {
                                                                                {
                                                                                    {
                                                                                        // TODO print stack trace
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                });
            }
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
