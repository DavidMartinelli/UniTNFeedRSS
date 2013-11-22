package it.unitn.hci.feed.android;

import it.unitn.hci.feed.android.utils.CallbackAsyncTask;
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

public class UnitnApiAsync
{
    public static String IP;

    static
    {
        try
        {
            IP = StreamUtils.readAll(App.getContext().getAssets().open("ip.txt"));
        }
        catch (Exception e)
        {
            // Should never happen
            e.printStackTrace();
            IP = "10.23.81.179";
        }
    }
    public static final String PROTOCOL = "http";
    public static final int PORT = 6767;
    public static final String PATH = "/rssservice/";
    public static final String BASE_URL = PROTOCOL + "//" + IP + ":" + PORT + PATH;


    private UnitnApiAsync()
    {
        // static methods only
    }


    public static CallbackAsyncTask<List<Course>> getCoursesAsync(final String department, final Action<TaskResult<List<Course>>> callback)
    {
        CallbackAsyncTask<List<Course>> task = new CallbackAsyncTask<List<Course>>(callback)
        {
            @Override
            public List<Course> doJob() throws Exception
            {
                return getCourses(department);
            }
        };
        task.execute();

        return task;
    }


    public static List<Course> getCourses(String department) throws Exception
    {
        List<Course> deparments;

        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + "courses/" + department, null, null);
        HttpGet get = new HttpGet(uri);

        HttpEntity entity = null;

        try
        {
            HttpResponse response = client.execute(get);

            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() != 200) throw new FileNotFoundException(line.getStatusCode() + ": " + line.getReasonPhrase());

            entity = response.getEntity();
            String json = StreamUtils.readAll(entity.getContent());
            JSONArray jsonArray = new JSONObject(json).getJSONArray("course");

            deparments = new ArrayList<Course>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                deparments.add(new Course(o.getInt("id"), o.getString("name"), o.getInt("colour"), null));
            }
            return deparments;
        }
        finally
        {
            if (entity != null) entity.consumeContent();
        }
    }


    public static CallbackAsyncTask<List<String>> getDepartmentsAsync(final Action<TaskResult<List<String>>> callback)
    {
        CallbackAsyncTask<List<String>> task = new CallbackAsyncTask<List<String>>(callback)
        {
            @Override
            public List<String> doJob() throws Exception
            {
                return getDepartments();
            }
        };
        task.execute();

        return task;
    }


    public static List<String> getDepartments() throws Exception
    {
        List<String> deparments;

        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + "departments", null, null);
        HttpGet get = new HttpGet(uri);

        HttpEntity entity = null;

        try
        {
            HttpResponse response = client.execute(get);

            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() != 200) throw new FileNotFoundException(line.getStatusCode() + ": " + line.getReasonPhrase());

            entity = response.getEntity();
            String json = StreamUtils.readAll(entity.getContent());
            JSONArray jsonArray = new JSONObject(json).getJSONArray("department");

            deparments = new ArrayList<String>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++)
            {
                deparments.add(jsonArray.getJSONObject(i).getString("name"));
            }
            return deparments;
        }
        finally
        {
            if (entity != null) entity.consumeContent();
        }
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

            JSONArray jsonArray = new JSONObject(json).getJSONArray("feed");
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
