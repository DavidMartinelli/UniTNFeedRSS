package it.unitn.hci.feed.android;

import it.unitn.hci.feed.android.utils.SharedUtils;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import it.unitn.hci.utils.OsUtils;
import java.util.List;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RssService extends Service
{

    private static final long SLEEP_TIME = 900000;
    private boolean mStop = false;


    @Override
    public IBinder onBind(Intent intent)
    {
        return (IBinder) this;
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
            List<Long> ids = SharedUtils.getCourses(this);
            for (long id : ids)
            {
                try
                {
                    getFeeds(id);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    // UPS, Kuuuupa
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // thank you java!
        }
    }


    private void getFeeds(long courseId) throws Exception
    {
        DatabaseManager manager = DatabaseManager.instantiate(this);
        Course c = manager.getCourse(courseId);
        List<Feed> feeds = UnitnApi.getFeeds(c, manager.getLastFeed(c));
        manager.insertFeeds(feeds, c);
        
        // TODO mandare la push
    }

}
