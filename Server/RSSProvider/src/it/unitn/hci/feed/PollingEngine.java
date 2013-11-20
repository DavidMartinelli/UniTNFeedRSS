package it.unitn.hci.feed;

import it.unitn.hci.feed.common.models.Feed;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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
    private static final int SLEEP_MILLIS = 600000; //1 minute
    private static final String UNITN_FEED_PATH = "http://www.science.unitn.it/cisca/avvisi/avvisi.php";

    private final URL UNITN_FEED_URL;
    private final Logger LOGGER;
    private long mModifiedSince;
    private static final List<Feed> mCache = new ArrayList<Feed>();


    public PollingEngine()
    {
        LOGGER = Logger.getLogger("resources_provider");
        mModifiedSince = -1;
        try
        {
            UNITN_FEED_URL = new URL(UNITN_FEED_PATH);
        }
        catch (MalformedURLException e)
        {
            // should never happen
            throw new RuntimeException(e);
        }
    }


    private void pollFromUnitn() throws Exception
    {
        System.out.println("===== PollingEngine: wake up...");

        HttpURLConnection connection = (HttpURLConnection) UNITN_FEED_URL.openConnection();
        connection.connect();

        final long lastModified = connection.getLastModified();
        if (lastModified != 0 && connection.getLastModified() < mModifiedSince)
        {
            connection.disconnect();
            System.out.println("==== PollingEngine: nothing new, going to sleep...");
            return;
        }

        mModifiedSince = System.currentTimeMillis();
        final Document page = Jsoup.parse(connection.getInputStream(), "ISO-8859-1", UNITN_FEED_PATH);
        connection.disconnect();
        Elements feeds = page.select("table.avviso tbody tr td font[size=5]");

        synchronized (mCache)
        {
            mCache.clear();
            for (Element feed : feeds)
            {
                Feed f = FeedAnalyzer.extract(feed.text());
                mCache.add(f);
            }
        }

        System.out.println("===== PollingEngine: now sleeps...");
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
                LOGGER.log(Level.SEVERE, e.toString());
            }
        }
    }


    public static List<Feed> getCache()
    {
        return mCache;
    }
}
