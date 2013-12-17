package it.unitn.hci.feed.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FeedNotificator extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent myIntent = new Intent(context, RssService.class);
        context.startService(myIntent);
    }
}
