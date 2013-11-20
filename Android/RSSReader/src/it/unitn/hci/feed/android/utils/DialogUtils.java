package it.unitn.hci.feed.android.utils;

import it.unitn.hci.feed.R;
import android.content.Context;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class DialogUtils
{

    public static void showPopupWindowMenu(Context context, View anchor, OnClickListener manageFeedsListener, OnClickListener showAllFeedsListener, OnClickListener showNotificationListener)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fast_menu_layout, null);

        final PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAsDropDown(anchor);
    }
}
