package it.unitn.hci.feed;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import it.unitn.hci.feed.common.models.Alias;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;

public class DatabaseManager
{

    private static final ThreadLocal<DatabaseManager> connectionPool = new ThreadLocal<DatabaseManager>();
    private static final String DATABASE_NAME = "feeds.db";
    private ConnectionSource mConnectionSource;


    public static DatabaseManager fromConnectionPool() throws Exception
    {
        DatabaseManager local = connectionPool.get();
        if (local == null || !local.mConnectionSource.isOpen())
        {
            local = createConnection(DATABASE_NAME);
        }
        return local;
    }


    private static DatabaseManager createConnection(String databaseName) throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        return new DatabaseManager("jdbc:sqlite:" + databaseName);
    }


    private DatabaseManager(String jdbcURL)
    {
        try
        {
            mConnectionSource = new JdbcConnectionSource(jdbcURL);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    private static Dao<Feed, Integer> createFeedDao(DatabaseManager db) throws Exception
    {
        return DaoManager.createDao(db.mConnectionSource, Feed.class);
    }


    private static Dao<Course, Integer> createCourseDao(DatabaseManager db) throws Exception
    {
        return DaoManager.createDao(db.mConnectionSource, Course.class);
    }


    private static Dao<Department, Integer> createDepartmentDao(DatabaseManager db) throws Exception
    {
        return DaoManager.createDao(db.mConnectionSource, Department.class);
    }


    private static Dao<Alias, Integer> createAliasDao(DatabaseManager db) throws Exception
    {
        return DaoManager.createDao(db.mConnectionSource, Alias.class);
    }


    private void close() throws Exception
    {
        mConnectionSource.close();
    }


    private static void close(DatabaseManager db) throws Exception
    {
        if (db != null) db.close();
    }


    public static void init() throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            TableUtils.createTableIfNotExists(db.mConnectionSource, Department.class);
            TableUtils.createTableIfNotExists(db.mConnectionSource, Course.class);
            TableUtils.createTableIfNotExists(db.mConnectionSource, Alias.class);
            TableUtils.createTableIfNotExists(db.mConnectionSource, Feed.class);

            insertDepartments(ResourceParser.getDepartments());
        }
        finally
        {
            close(db);
        }
    }


    public static void insertFeed(Feed feed) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            insertFeed(feed, createFeedDao(db));
        }
        finally
        {
            close(db);
        }
    }


    private static void insertFeed(Feed feed, Dao<Feed, Integer> feedDao) throws Exception
    {
        if (feed.getCourse() == null) throw new IllegalArgumentException("You must supply a course");
        feedDao.create(feed);
    }


    public static void insertCourse(Course course) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            Dao<Course, Integer> courseDao = createCourseDao(db);
            if (course.getDepartment() == null) throw new IllegalArgumentException("You must supply a department");

            courseDao.create(course);
            if (course.getFeeds() == null) return;

            final Dao<Feed, Integer> feedDao = createFeedDao(db);
            for (Feed f : course.getFeeds())
            {
                f.setCourse(course);
                insertFeed(f, feedDao);
            }

            final Dao<Alias, Integer> aliasDao = createAliasDao(db);
            for (Alias alias : course.getAliases())
            {
                alias.setCourse(course);
                aliasDao.create(alias);
            }

        }
        finally
        {
            close(db);
        }
    }


    public static List<Feed> insertFeeds(List<Feed> feeds) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            Dao<Feed, Integer> feedDao = createFeedDao(db);
            Set<Feed> storedFeeds = new HashSet<Feed>(getFeeds());

            List<Feed> newFeeds = new ArrayList<Feed>();
            for (Feed feed : feeds)
            {
                if (feed.getCourse() == null || !feed.getCourse().isPersistent()) throw new IllegalArgumentException("You must supply a course");

                boolean any = false;
                for (Feed stored : storedFeeds)
                {
                    if (stored.equals(feed))
                    {
                        any = true;
                        break;
                    }
                }

                if (!any)
                {
                    insertFeed(feed, feedDao);
                    newFeeds.add(feed);
                }
            }
            return newFeeds;
        }
        finally
        {
            close(db);
        }
    }


    public static List<Department> getDepartments() throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            return createDepartmentDao(db).queryForAll();
        }
        finally
        {
            close(db);
        }
    }


    public static List<Feed> getFeeds() throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            return createFeedDao(db).queryForAll();
        }
        finally
        {
            close(db);
        }
    }


    public static void insertDepartment(Department department) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            insertDepartment(department, createDepartmentDao(db), createCourseDao(db), createAliasDao(db), createFeedDao(db));
        }
        finally
        {
            close(db);
        }
    }


    public static void insertDepartments(List<Department> departments) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            final Dao<Department, Integer> dao = createDepartmentDao(db);
            final Dao<Course, Integer> courseDao = createCourseDao(db);
            final Dao<Alias, Integer> aliasDao = createAliasDao(db);
            final Dao<Feed, Integer> feedDao = createFeedDao(db);

            for (Department department : departments)
                insertDepartment(department, dao, courseDao, aliasDao, feedDao);
        }
        finally
        {
            close(db);
        }
    }


    private static void insertDepartment(Department department, Dao<Department, Integer> departmentDao, Dao<Course, Integer> courseDao, Dao<Alias, Integer> aliasDao, Dao<Feed, Integer> feedDao) throws Exception
    {
        departmentDao.createOrUpdate(department);

        for (Course c : department.getCourses())
        {
            c.setDepartment(department);
            courseDao.createOrUpdate(c);

            if (c.getAliases() != null)
            {
                for (Alias a : c.getAliases())
                {
                    a.setCourse(c);
                    aliasDao.createOrUpdate(a);
                }
            }

            if (c.getFeeds() != null)
            {
                for (Feed f : c.getFeeds())
                {
                    f.setCourse(c);
                    feedDao.create(f);
                }
            }
        }

    }


    public static List<Feed> getFeeds(long lastRecivedCourseId, String courseName) throws Exception
    {
        Course course = getCourse(courseName);

        List<Feed> f = new ArrayList<Feed>();
        for (Feed s : course.getFeeds())
            if (s.getId() > lastRecivedCourseId) f.add(s);

        return f;
    }


    public static Collection<Feed> getFeeds(String courseName) throws Exception
    {
        return getCourse(courseName).getFeeds();
    }


    private static Course getCourse(String courseName) throws Exception
    {
        DatabaseManager db = null;
        try
        {
            db = fromConnectionPool();
            Dao<Course, Integer> dao = createCourseDao(db);

            Course course = dao.queryForEq("mName", courseName).get(0);
            if (course == null) throw new FileNotFoundException("Course named \"" + courseName + "\" does not exist");
            return course;
        }
        finally

        {
            close(db);
        }
    }


    public static void main(String[] argv) throws Exception
    {
        init();
    }

}
