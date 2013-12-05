package it.unitn.hci.feed.android;

import it.unitn.hci.feed.android.utils.CallbackAsyncTask;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.Action;
import it.unitn.hci.feed.android.utils.CallbackAsyncTask.TaskResult;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
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

public class UnitnApi
{
    private static String IP = "192.168.0.103";
    private static final String PROTOCOL = "http";
    private static final int PORT = 6767;
    private static final String PATH = "/rssservice/";


    private UnitnApi()
    {
        // static methods only
    }


    public static CallbackAsyncTask<List<Course>> getCoursesAsync(final Department department, final Action<TaskResult<List<Course>>> callback)
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


    public static List<Course> getCourses(Department department) throws Exception
    {
        List<Course> deparments;

        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + "departments/" + department.getId(), null, null);
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


    public static CallbackAsyncTask<List<Department>> getDepartmentsAsync(final Action<TaskResult<List<Department>>> callback)
    {
        CallbackAsyncTask<List<Department>> task = new CallbackAsyncTask<List<Department>>(callback)
        {
            @Override
            public List<Department> doJob() throws Exception
            {
                return getDepartments();
            }
        };
        task.execute();

        return task;
    }


    public static List<Department> getDepartments() throws Exception
    {
        List<Department> deparments;

        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + "departments", null, null);

        HttpGet get = new HttpGet(uri);
        get.addHeader("Accept", "application/json");

        HttpEntity entity = null;

        try
        {
            HttpResponse response = client.execute(get);

            StatusLine line = response.getStatusLine();
            if (line.getStatusCode() != 200) throw new FileNotFoundException(line.getStatusCode() + ": " + line.getReasonPhrase());

            entity = response.getEntity();
            String json = StreamUtils.readAll(entity.getContent());
            JSONArray jsonArray = new JSONArray(json);
            deparments = new ArrayList<Department>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                Department tmp = new Department(o.getString("name"), null, null, null);
                tmp.setId(o.getLong("id"));
                deparments.add(tmp);
            }
            return deparments;
        }
        finally
        {
            if (entity != null) entity.consumeContent();
        }
    }


    public static CallbackAsyncTask<List<Feed>> getFeedsAsync(final Course course, final Long lastReceivedFeed, final Action<TaskResult<List<Feed>>> callback)
    {
        CallbackAsyncTask<List<Feed>> task = new CallbackAsyncTask<List<Feed>>(callback)
        {
            @Override
            public List<Feed> doJob() throws Exception
            {
                return getFeeds(course, lastReceivedFeed);
            }
        };
        task.execute();

        return task;
    }


    public static List<Feed> getFeeds(Course course, Long lastSavedFeed) throws Exception
    {
        HttpClient client = new DefaultHttpClient();
        URI uri = new URI(PROTOCOL, null, IP, PORT, PATH + course.getId(), "lastReceivedId=" + lastSavedFeed, null);
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

                final int id = jsonFeed.getInt("id");
                final String body = jsonFeed.getString("body");
                final long timestamp = jsonFeed.getLong("timestamp");

                feeds.add(new Feed(id, body, timestamp, course));
            }

            return feeds;
        }
        finally
        {
            if (entity != null) entity.consumeContent();
        }
    }
}
