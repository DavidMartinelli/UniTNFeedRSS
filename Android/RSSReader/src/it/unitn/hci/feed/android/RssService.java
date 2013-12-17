package it.unitn.hci.feed.android;

import it.unitn.hci.feed.android.models.Course;
import it.unitn.hci.feed.android.models.Feed;
import it.unitn.hci.utils.OsUtils;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

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


    @Override
    public void onCreate()
    {
        super.onCreate();
        mStop = false;
        System.out.println("start");
        new Thread()
        {
            @Override
            public void run()
            {
                loopForRss();
            }
        }.start();
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
            for (Course c : courses)
            {
                System.out.println("courrse " + c);
                try
                {
                    getFeeds(c.getId(), manager);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    // UPS, Kuuuupa
                }
            }
            DatabaseManager.close(manager);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // thank you java!
        }
    }


    private void getFeeds(long courseId, DatabaseManager manager) throws Exception
    {
        Course c = manager.getCourse(courseId);
        List<Feed> feeds = UnitnApi.getFeeds(c, manager.getLastFeed(c));
        manager.insertFeeds(feeds, c);
        // TODO mandare la push
        mIntent = new Intent(MainActivity.DIALOG_INTENT);
        sendBroadcast(mIntent);
    }

    public class LocalBinder extends Binder
    {
        public RssService getServerInstance()
        {
            return RssService.this;
        }
    }

}
