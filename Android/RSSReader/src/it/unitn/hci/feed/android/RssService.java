package it.unitn.hci.feed.android;

import it.unitn.hci.feed.R;
import it.unitn.hci.feed.android.models.Course;
import it.unitn.hci.feed.android.models.Feed;
import it.unitn.hci.feed.android.utils.RefreshTask;
import it.unitn.hci.utils.OsUtils;
import java.util.List;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RssService extends Service
{

    private static final long SLEEP_TIME = 900000;
    private boolean mStop = false;
    private final IBinder mLocalBinder = new LocalBinder();
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent)
    {
        return mLocalBinder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return Service.START_NOT_STICKY;
    }

    private Thread mPollingThread;
    public Thread getPollingThread()
    {
        return mPollingThread;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        mStop = false;
        System.out.println("start");
        mPollingThread = new Thread()
        {
            @Override
            public void run()
            {
                loopForRss();
            }
        };
        mPollingThread.start();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mStop = true;
    }


    private void loopForRss()
    {
        while (!mStop)
        {
            System.out.println("start thread");
            getFeeds();
            OsUtils.vaFateNaDormida(SLEEP_TIME);
        }
    }


    private void getFeeds()
    {
        try
        {
            DatabaseManager manager = DatabaseManager.instantiate(this);
            List<Course> courses = manager.getCourses();
            System.out.println("loop for courses " + courses);
            Integer totalNewFeed = 0;
            for (Course c : courses)
            {
                System.out.println("courrse " + c);
                try
                {
                    totalNewFeed += getFeeds(c.getId(), manager);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    // UPS, Kuuuupa
                }
            }
            DatabaseManager.close(manager);
            //aggiornare la home page con i feed
            if (totalNewFeed > 0) {
                sendBroadcast(new Intent(RefreshTask.REFRESH_DATA_INTENT));
                sendNotification();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // thank you java!
        }
    }

    /*
     * Return the number of new feeds
     */
    private int getFeeds(long courseId, DatabaseManager manager) throws Exception
    {
        Course c = manager.getCourse(courseId);
        List<Feed> feeds = UnitnApi.getFeeds(c, manager.getLastFeed(c));
        manager.insertFeeds(feeds, c);
        if (feeds != null && feeds.size() > 0 ){
            return feeds.size();
        }
        return 0;
    }
    
    private void sendNotification(){
        NotificationManager mNM =
                (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(R.drawable.ic_launcher, getString(R.string.new_feeds), System.currentTimeMillis());
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new
                Intent(this, MainActivity.class), 0);
        
        notification.setLatestEventInfo(this, getText(R.string.app_name), getString(R.string.new_feeds), contentIntent);
        
        mNM.notify(153, notification);
    }

    public class LocalBinder extends Binder
    {
        public RssService getServerInstance()
        {
            return RssService.this;
        }
    }

}
