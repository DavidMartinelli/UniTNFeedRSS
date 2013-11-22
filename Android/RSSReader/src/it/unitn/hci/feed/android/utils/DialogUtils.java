package it.unitn.hci.feed.android.utils;

import java.util.ArrayList;
import java.util.List;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.adapter.CourseAdapter;
import it.unitn.hci.feed.android.adapter.DepartmentAdapter;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DialogUtils
{

    @SuppressWarnings("deprecation")
    public static void showPopupWindowMenu(Context context, View anchor, OnClickListener manageFeedsListener, OnClickListener showAllFeedsListener, OnClickListener showNotificationListener)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fast_menu_layout, null);

        TextView btnManageFeeds = (TextView) view.findViewById(R.id.btnManageFeeds);
        TextView btnShowAllFeeds = (TextView) view.findViewById(R.id.btnShowAllFeeds);
        TextView btnEnableNotification = (TextView) view.findViewById(R.id.btnEnableNotification);

        btnManageFeeds.setOnClickListener(manageFeedsListener);
        btnShowAllFeeds.setOnClickListener(showAllFeedsListener);
        btnEnableNotification.setOnClickListener(showNotificationListener);

        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor);
    }


    public static void showFeed(final Context context, final Feed feed)
    {
        final DialogFragment d = new DialogFragment()
        {
            @Override
            public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState)
            {
                View rootView = inflater.inflate(R.layout.feed_dialog_layout, container, false);
                TextView lblTitle = (TextView) rootView.findViewById(R.id.lblTitle);
                TextView lblBody = (TextView) rootView.findViewById(R.id.lblBody);
                View lyTitle = rootView.findViewById(R.id.lyTitle);
                lyTitle.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dismiss();
                    }
                });

                lblTitle.setText(feed.getCourse().getStringName());
                lblBody.setText(feed.getBody());

                return rootView;
            }


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState)
            {
                Dialog dialog = super.onCreateDialog(savedInstanceState);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }
        };

        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();

        d.setCancelable(true);
        d.show(manager, "full_feed");
    }


    public static void showDepartmentsList(final Context context, final List<String> departments, final OnItemClickListener listener)
    {
        final DialogFragment d = new DialogFragment()
        {
            @Override
            public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState)
            {
                View rootView = inflater.inflate(R.layout.departments_chooser_layout, container, false);
                View lyTitle = rootView.findViewById(R.id.lyTitle);
                lyTitle.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dismiss();
                    }
                });

                ListView lstDepartments = (ListView) rootView.findViewById(R.id.lstDepartments);
                lstDepartments.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
                    {
                        listener.onItemClick(arg0, arg1, arg2, arg3);
                        dismiss();
                    }
                });
                DepartmentAdapter adapter = new DepartmentAdapter(context, departments);
                lstDepartments.setAdapter(adapter);

                return rootView;
            }


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState)
            {
                Dialog dialog = super.onCreateDialog(savedInstanceState);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }
        };

        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();

        d.setCancelable(true);
        d.show(manager, "departments");
    }


    public static void showCoursesSelector(final Context context, final List<Course> courses, final Action<List<Course>> action)
    {
        final DialogFragment d = new DialogFragment()
        {
            @Override
            public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState)
            {
                View rootView = inflater.inflate(R.layout.courses_chooser_layout, container, false);
                View lyTitle = rootView.findViewById(R.id.lyTitle);
                final ListView lstDepartments = (ListView) rootView.findViewById(R.id.lstCourses);
                View button = rootView.findViewById(R.id.btnOk);
                button.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        List<Course> result = new ArrayList<Course>();
                        for (int i = 0; i < lstDepartments.getChildCount(); i++)
                        {
                            //TODO ricavare i field con il cb selected e ritornarli
                        }
                        action.invoke(result);
                    }
                });

                lyTitle.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dismiss();
                    }
                });

                CourseAdapter adapter = new CourseAdapter(context, courses);
                lstDepartments.setAdapter(adapter);

                return rootView;
            }


            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState)
            {
                Dialog dialog = super.onCreateDialog(savedInstanceState);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                return dialog;
            }
        };

        FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();

        d.setCancelable(true);
        d.show(manager, "departments");
    }
}
