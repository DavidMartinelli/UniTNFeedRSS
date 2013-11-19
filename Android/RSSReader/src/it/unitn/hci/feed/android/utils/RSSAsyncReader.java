package it.unitn.hci.feed.android.utils;

import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class RSSAsyncReader
{
    public static final String PROTOCOL = "http";
    public static final int PORT = 6767;
    public static final String IP = "192.168.0.106";
    public static final String PATH = "/RssService/";
    public static final String BASE_URL = PROTOCOL + "//" + IP + ":" + PORT + PATH;


    private RSSAsyncReader()
    {
        // static methods only
    }


    public static CallbackAsyncTask<List<Feed>> getFeedsAsync(final Course course, final Action<TaskResult<List<Feed>>> callback)
    {
        CallbackAsyncTask<List<Feed>> task = new CallbackAsyncTask<List<Feed>>(callback)
        {
            @Override
            public List<Feed> doJob() throws Exception
            {
                return getFeeds(course);
            }
        };
        task.execute();

        return task;
    }


    public static List<Feed> getFeeds(Course course) throws Exception
    {
        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + course, null, null);
        HttpGet get = new HttpGet(uri);

        HttpEntity response = null;

        try
        {
            response = client.execute(get).getEntity();
            System.out.println(extractString(response));
            return null;
        }
        finally
        {
            if (response != null) response.consumeContent();
        }
    }


    private static String extractString(HttpEntity entity) throws Exception
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            StringBuilder b = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                b.append(line);
            }
            return b.toString();
        }
        finally
        {
            if (reader != null) reader.close();
        }
    }

}
