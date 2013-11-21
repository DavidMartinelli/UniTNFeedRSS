package it.unitn.hci.feed.android.utils;

import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import it.unitn.hci.utils.StreamUtils;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class RSSAsyncReader
{
    public static final String PROTOCOL = "http";
    public static final int PORT = 6767;
    public static final String IP = "10.23.78.110";
    public static final String PATH = "/RssService/";
    public static final String BASE_URL = PROTOCOL + "//" + IP + ":" + PORT + PATH;


    private RSSAsyncReader()
    {
        // static methods only
    }


    public static CallbackAsyncTask<List<Feed>> getFeedsAsync(final String courseName, final Action<TaskResult<List<Feed>>> callback)
    {
        CallbackAsyncTask<List<Feed>> task = new CallbackAsyncTask<List<Feed>>(callback)
        {
            @Override
            public List<Feed> doJob() throws Exception
            {
                return getFeeds(courseName);
            }
        };
        task.execute();

        return task;
    }


    public static List<Feed> getFeeds(String courseName) throws Exception
    {
        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + courseName, null, null);
        HttpGet get = new HttpGet(uri);

        HttpEntity entity = null;

        try
        {
            HttpResponse response = client.execute(get);

            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() != 200) throw new FileNotFoundException(line.getStatusCode() + ": " + line.getReasonPhrase());

            entity = response.getEntity();
            String json = StreamUtils.readAll(entity.getContent());

            JSONArray jsonArray = new JSONArray(json);
            List<Feed> feeds = new ArrayList<Feed>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonFeed = jsonArray.getJSONObject(i);
                JSONObject jsonCourse = jsonFeed.getJSONObject("course");

                final int id = jsonFeed.getInt("id");
                final String body = jsonFeed.getString("body");
                final long timestamp = jsonFeed.getLong("timestamp");
                
                final int colour = jsonCourse.getInt("colour");

                feeds.add(new Feed(id, body, timestamp, Course.notCached(courseName, colour)));
            }

            return feeds;
        }
        finally
        {
            if (entity != null) entity.consumeContent();
        }
    }
}
