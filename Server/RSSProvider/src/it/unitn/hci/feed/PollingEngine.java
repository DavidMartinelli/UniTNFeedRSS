package it.unitn.hci.feed;

import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;
import it.unitn.hci.utils.GCMUtils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PollingEngine extends Thread
{
    private static final int SLEEP_MILLIS = 600000; // 1 minute

    private final Logger LOGGER;
    private long mModifiedSince;


    public PollingEngine()
    {
        LOGGER = Logger.getLogger("resources_provider");
        mModifiedSince = -1;
        try
        {
            DatabaseManager.init();
        }
        catch (Exception e)
        {
            // should never happen
            throw new RuntimeException(e);
        }
    }


    private void pollFromUnitn() throws Exception
    {
        System.out.println("===== PollingEngine: wake up...");

        for (Department d : DatabaseManager.getDepartments())
        {
            try
            {
                pollNewsPage(d);
            }
            catch (Exception e)
            {
                // something bad happened, failing cowardly
                e.printStackTrace();
            }
        }

        System.out.println("===== PollingEngine: now sleeps...\n");
    }


    private void pollNewsPage(Department department) throws Exception
    {
        final URL newsPageUrl = new URL(department.getBulletinNewsURL());
        final String cssSelector = department.getCSSSelector();
        try
        {
            final String path = newsPageUrl.getPath();

            System.out.println("PollingEngine: dumping " + path + "...");

            HttpURLConnection connection = (HttpURLConnection) newsPageUrl.openConnection();
            connection.connect();

            final long lastModified = connection.getLastModified();
            if (lastModified != 0 && connection.getLastModified() < mModifiedSince)
            {
                connection.disconnect();
                System.out.println("PollingEngine: nothing new on " + path + ", going to sleep...");
                return;
            }

            mModifiedSince = System.currentTimeMillis();
            final Document page = Jsoup.parse(connection.getInputStream(), "ISO-8859-1", path);
            connection.disconnect();
            Elements feedNodes = page.select(cssSelector);

            List<Feed> feeds = new ArrayList<Feed>(feedNodes.size());
            final long timeStamp = System.currentTimeMillis();
            for (Element feed : feedNodes)
            {
                Feed f = FeedAnalyzer.extract(feed.text(), timeStamp, department);
                feeds.add(f);
            }

            feeds = DatabaseManager.insertFeeds(feeds);
            if (feeds != null && !feeds.isEmpty())
            {
                System.out.println("PollingEngine: found " + feeds.size() + " new feeds on " + path + ". Calling GCM...");
                GCMUtils.notify(feeds);
            }
            else
            {
                System.out.println("PollingEngine: nothing new on " + path + ", going to sleep...");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.out.println();
    }


    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                pollFromUnitn();
                sleep(SLEEP_MILLIS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, e.toString());
            }
        }
    }

}
