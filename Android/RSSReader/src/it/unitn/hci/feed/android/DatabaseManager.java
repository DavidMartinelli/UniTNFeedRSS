package it.unitn.hci.feed.android;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;
import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager
{
    private static final String DATABASE_NAME = "feeds.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseManager mManager;

    private ConnectionSource mConnectionSource;
    private final Dao<Course, Integer> mCourseDao;
    private final Dao<Feed, Integer> mFeedDao;


    private DatabaseManager(Context context)
    {
        final OrmLiteSqliteOpenHelper helper = new OrmLiteSqliteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
        {

            @Override
            public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3)
            {
                // empty method
            }


            @Override
            public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
            {
                try
                {
                    if (mConnectionSource == null) mConnectionSource = new AndroidConnectionSource(this);
                    TableUtils.createTableIfNotExists(mConnectionSource, Course.class);
                    TableUtils.createTableIfNotExists(mConnectionSource, Feed.class);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        };
        mConnectionSource = new AndroidConnectionSource(helper);
        try
        {
            mCourseDao = DaoManager.createDao(mConnectionSource, Course.class);
            mFeedDao = DaoManager.createDao(mConnectionSource, Feed.class);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static DatabaseManager instantiate(Context context)
    {
        if (mManager == null) mManager = new DatabaseManager(context);
        return mManager;
    }


    public Long getLastFeed(Course course) throws Exception
    {
        Collection<Feed> feeds = course.getFeeds();
        Long mostRecent = Long.MIN_VALUE;
        for (Feed f : feeds)
            if (f.getId() > mostRecent) mostRecent = (long) f.getId();

        return mostRecent;
    }


    public void insertFeeds(List<Feed> feeds, Course course) throws Exception
    {
        for (Feed feed : feeds)
        {
            feed.setCourse(course);
            mFeedDao.createIfNotExists(feed);
        }
    }


    public Course getCourse(long courseId) throws Exception
    {
        return mCourseDao.queryForId((int) courseId);
    }


    public List<Feed> getFeeds(List<Long> coursesId) throws Exception
    {
        List<Feed> feeds = new ArrayList<Feed>();
        for (Long courseId : coursesId)
        {
            Course course = getCourse(courseId);
            feeds.addAll(course.getFeeds());
        }
        Collections.sort(feeds);
        return feeds;
    }


    public void saveCourse(Course course, long id) throws Exception
    {
        course.setId(id);
        mCourseDao.createIfNotExists(course);
    }


    public void deleteCourse(Course course) throws Exception
    {
        mCourseDao.deleteById(course.getId());
        for (Feed f : course.getFeeds())
            mFeedDao.deleteById(f.getId());
    }


    public void syncCourses(List<Course> courses) throws Exception
    {
        List<Course> savedCourses = mCourseDao.queryForAll();
        List<Course> toDelete = new ArrayList<Course>();

        for (Course savedCourse : savedCourses)
        {
            boolean any = false;
            for (Course course : courses)
            {
                if (course.getId() == savedCourse.getId())
                {
                    any = true;
                    break;
                }
            }
            if (!any) toDelete.add(savedCourse);
        }

        for (Course delete : toDelete)
            deleteCourse(delete);

        for (Course save : courses)
            saveCourse(save, save.getId());
    }
}
