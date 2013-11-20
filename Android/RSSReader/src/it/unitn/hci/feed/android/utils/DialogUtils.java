package it.unitn.hci.feed.android.utils;

import it.unitn.hci.feed.R;
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
import android.widget.PopupWindow;
import android.widget.TextView;

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


    public static void showListOfSongs(final Context context, final Feed feed)
    {
        DialogFragment d = new DialogFragment()
        {

            @Override
            public View onCreateView(LayoutInflater inflater, android.view.ViewGroup container, android.os.Bundle savedInstanceState)
            {
                View rootView = inflater.inflate(R.layout.feed_dialog_layout, container, false);
                TextView lblTitle = (TextView) rootView.findViewById(R.id.lblTitle);
                TextView lblBody = (TextView) rootView.findViewById(R.id.lblBody);

                lblTitle.setText(feed.getSubject().getStringName());
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
}
