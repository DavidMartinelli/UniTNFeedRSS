package it.unitn.hci.feed.android.utils;

import java.util.ArrayList;
import java.util.List;
import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.adapter.AllFeedsAdapter;
import it.unitn.hci.feed.android.adapter.CourseAdapter;
import it.unitn.hci.feed.android.adapter.DepartmentAdapter;
import it.unitn.hci.feed.android.models.Course;
import it.unitn.hci.feed.android.models.Department;
import it.unitn.hci.feed.android.models.Feed;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DialogUtils
{

    public static ProgressDialog showProgressDialog(Context c, String title, String message, boolean isCancellable)
    {
        final ProgressDialog dialog = new ProgressDialog(c);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.show();

        return dialog;
    }


    public static void show(String message, String title, Context context, boolean isCancellable, Integer icon, String positiveButtonText, DialogInterface.OnClickListener positiveButtonListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        if (title != null) builder.setTitle(title);
        builder.setCancelable(isCancellable);
        if (icon != null) builder.setIcon(icon);
        if (positiveButtonText != null) builder.setPositiveButton(positiveButtonText, positiveButtonListener);
        AlertDialog alert = builder.create();
        alert.show();
    }


    //
    @SuppressWarnings("deprecation")
    public static void showPopupWindowMenu(Context context, View anchor, final OnClickListener manageFeedsListener, final OnClickListener showAllFeedsListener, final OnClickListener showNotificationListener) throws Exception
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fast_menu_layout, null);

        TextView btnManageFeeds = (TextView) view.findViewById(R.id.btnManageFeeds);
        TextView btnShowAllFeeds = (TextView) view.findViewById(R.id.btnShowAllFeeds);
        TextView btnEnableNotification = (TextView) view.findViewById(R.id.btnEnableNotification);
        btnEnableNotification.setText(SharedUtils.isNotificationsEnabled(context) ? context.getString(R.string.disable_notifications) : context.getString(R.string.enable_notifications));

        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor);

        btnManageFeeds.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
                manageFeedsListener.onClick(v);
            }
        });
        btnShowAllFeeds.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
                showAllFeedsListener.onClick(v);
            }
        });
        btnEnableNotification.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                popupWindow.dismiss();
                showNotificationListener.onClick(v);
            }
        });

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


    public static void showDepartmentsList(final Context context, final List<Department> departments, final OnItemClickListener listener)
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
                final CourseAdapter adapter = new CourseAdapter(context, courses);
                lstDepartments.setAdapter(adapter);

                button.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        List<Course> result = new ArrayList<Course>();
                        List<Boolean> selectedCourse = adapter.getSelected();
                        for (int i = 0; i < courses.size(); i++)
                        {
                            if (selectedCourse.get(i)) result.add(courses.get(i));
                        }

                        action.invoke(result);
                        dismiss();
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


    public static void showAllFeeds(final Context context, final List<Feed> feeds)
    {
        DialogFragment d = new DialogFragment()
        {
            Button btn;
            ListView lstFields;


            @Override
            public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState)
            {
                View rootView = inflater.inflate(R.layout.show_all_feeds_dialog_layout, container, false);
                lstFields = (ListView) rootView.findViewById(R.id.lstMenu);

                btn = (Button) rootView.findViewById(R.id.btnDone);
                btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dismiss();
                    }
                });

                final AllFeedsAdapter a = new AllFeedsAdapter(context, feeds);
                lstFields.setAdapter(a);
                lstFields.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
                    {
                        Feed f = feeds.get(position);
                        if (f != null)
                        {
                            showFeed(context, f);
                        }
                    }
                });
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

        d.show(manager, "all_feeds");
    }
}
