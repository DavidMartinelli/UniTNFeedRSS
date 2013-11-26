package it.unitn.hci.feed;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Department;
import it.unitn.hci.feed.common.models.Feed;
import it.unitn.hci.utils.ColourUtils;
import it.unitn.hci.utils.StreamUtils;
import it.unitn.hci.utils.TODOException;

public class DatabaseManager
{

    private DatabaseManager()
    {
        // static methods only
    }

    private final static String TABLE_DEPARTMENTS = "Departments";
    private final static String TABLE_DEPARTMENTS_COURSES = "DepartmentsCourses";
    private final static String TABLE_COURSES = "Courses";
    private final static String TABLE_USERS = "Users";
    private final static String TABLE_ALIASES = "Aliases";
    private final static String TABLE_USERS_COURSES = "Departments";
    private final static String TABLE_FEEDS = "Feeds";

    private final static String COLUMN_DEPARTMENT_NAME = "department_name";
    private final static String COLUMN_DEPARTMENT_ID = "department_id";
    private final static String COLUMN_DEPARTMENT_LINK = "departiment_link";
    private final static String COLUMN_DEPARTMENT_CSS_SELECTOR = "department_css_selector";
    private final static String COLUMN_DEPARTMENT_COURSE_ID = "departments_course_id";
    private final static String COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS = "departments_course_fk_departments";
    private final static String COLUMN_DEPARTMENT_COURSE_FK_COURSES = "departments_course_fk_courses";
    private final static String COLUMN_COURSE_ID = "course_id";
    private final static String COLUMN_COURSE_NAME = "course_name";
    private final static String COLUMN_COURSE_COLOUR = "course_colour";
    private final static String COLUMN_ALIAS_ID = "alias_id";
    private final static String COLUMN_ALIAS_VALUE = "alias_value";
    private final static String COLUMN_ALIAS_FK_COURSE = "alias_fk_course";
    private final static String COLUMN_USER_COURSE_ID = "user_course_id";
    private final static String COLUMN_USER_COURSE_FK_COURSES = "user_course_fk_courses";
    private final static String COLUMN_USER_COURSE_FK_USERS = "user_course_fk_users";
    private final static String COLUMN_USER_ID = "user_id";
    private final static String COLUMN_USER_TOKEN = "user_token";
    private final static String COLUMN_FEED_ID = "feed_id";
    private final static String COLUMN_FEED_BODY = "feed_body";
    private final static String COLUMN_FEED_FK_COURSE = "feed_fk_course";
    private final static String COLUMN_FEED_TIMESTAMP = "feed_timestamp";

    private final static String CREATE_TABLE_DEPARTMENRS = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENTS + " ( " + COLUMN_DEPARTMENT_ID + " INTEGER PRIMARY KEY, " + COLUMN_DEPARTMENT_NAME + " VARCHAR(200), " + COLUMN_DEPARTMENT_LINK + " VARCHAR(500), " + COLUMN_DEPARTMENT_CSS_SELECTOR + " VARCHAR(200))";
    private final static String CREATE_TABLE_DEPARTMENTS_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENTS_COURSES + " ( " + COLUMN_DEPARTMENT_COURSE_ID + " INTEGER PRIMARY KEY, " + COLUMN_DEPARTMENT_COURSE_FK_COURSES + " REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "), " + COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS + " REFERENCES " + TABLE_DEPARTMENTS + "(" + COLUMN_DEPARTMENT_ID + "))";
    private final static String CREATE_TABLE_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " ( " + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY, " + COLUMN_COURSE_NAME + " VARCHAR(60), " + COLUMN_COURSE_COLOUR + " INTEGER UNIQUE)";
    private final static String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ( " + COLUMN_USER_ID + " INTEGER PRIMARY KEY, " + COLUMN_USER_TOKEN + " VARCHAR(256))";
    private final static String CREATE_TABLE_ALIASES = "CREATE TABLE IF NOT EXISTS " + TABLE_ALIASES + " ( " + COLUMN_ALIAS_ID + " INTEGER PRIMARY KEY, " + COLUMN_ALIAS_VALUE + " VARCHAR(70), " + COLUMN_ALIAS_FK_COURSE + " REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))";
    private final static String CREATE_TABLE_USERS_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS_COURSES + " ( " + COLUMN_USER_COURSE_ID + " INTEGER PRIMARY KEY, " + COLUMN_USER_COURSE_FK_COURSES + " REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "), " + COLUMN_USER_COURSE_FK_USERS + " INTEGER REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
    private final static String CREATE_TABLE_FEED = "CREATE TABLE IF NOT EXISTS " + TABLE_FEEDS + " ( " + COLUMN_FEED_ID + " INTEGER PRIMARY KEY, " + COLUMN_FEED_FK_COURSE + " REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + ")," + COLUMN_FEED_TIMESTAMP + " INTEGER, " + COLUMN_FEED_BODY + " TEXT)";

    private final static String GET_ALIASES_BY_COURSE_NAME = "SELECT " + COLUMN_ALIAS_VALUE + " FROM " + TABLE_ALIASES + " JOIN " + TABLE_COURSES + " ON " + COLUMN_ALIAS_FK_COURSE + "=" + COLUMN_COURSE_ID + " WHERE " + COLUMN_COURSE_NAME + "=?";
    private final static String GET_ALL_COURSES = "SELECT * FROM " + TABLE_COURSES;
    private final static String GET_ALL_FEEDS = "SELECT * FROM " + TABLE_FEEDS + " JOIN " + TABLE_COURSES + " ON " + TABLE_FEEDS + "." + COLUMN_FEED_FK_COURSE + " = " + TABLE_COURSES + "." + COLUMN_COURSE_ID;
    private final static String GET_ALL_FEEDS_AFTER_ID_BY_COURSE_NAME = "SELECT * FROM " + TABLE_FEEDS + " JOIN " + TABLE_COURSES + " ON " + COLUMN_FEED_FK_COURSE + " = " + COLUMN_COURSE_ID + " WHERE " + COLUMN_COURSE_NAME + "=? AND " + COLUMN_COURSE_ID + ">?";
    private final static String GET_COURSES_COLOURS_AND_NAMES = "SELECT " + COLUMN_COURSE_COLOUR + ", " + COLUMN_COURSE_NAME + " FROM " + TABLE_COURSES;
    private final static String GET_COURSES_NAME_BY_DEPARTMENT_NAME = "SELECT * FROM " + TABLE_COURSES + " JOIN " + TABLE_DEPARTMENTS_COURSES + " ON " + COLUMN_COURSE_ID + "=" + COLUMN_DEPARTMENT_COURSE_FK_COURSES + " JOIN " + TABLE_DEPARTMENTS + " JOIN " + COLUMN_DEPARTMENT_ID + "=" + COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS + " WHERE " + COLUMN_DEPARTMENT_NAME + "=?";
    private final static String GET_FEEDS_BY_COURSE_NAME = "SELECT * FROM " + TABLE_FEEDS + " JOIN " + TABLE_COURSES + " ON " + COLUMN_COURSE_ID + "=" + COLUMN_FEED_FK_COURSE + " WHERE " + COLUMN_COURSE_NAME + "=?";
    private final static String GET_DEPARTMENTS = "SELECT " + COLUMN_DEPARTMENT_NAME + ", " + COLUMN_DEPARTMENT_LINK + ", " + COLUMN_DEPARTMENT_CSS_SELECTOR + " FROM " + TABLE_DEPARTMENTS;
    private final static String INSERT_ALIAS = "INSERT INTO " + TABLE_ALIASES + "(" + COLUMN_ALIAS_VALUE + ", " + COLUMN_ALIAS_FK_COURSE + ") VALUES ( ?, ?)";
    private final static String INSERT_COURSE = "INSERT INTO " + TABLE_COURSES + "(" + COLUMN_COURSE_COLOUR + ", " + COLUMN_COURSE_NAME + ") VALUES (?, ?)";
    private final static String INSERT_FEED = "INSERT INTO " + TABLE_FEEDS + "(" + COLUMN_FEED_BODY + ", " + COLUMN_FEED_FK_COURSE + ", " + COLUMN_FEED_TIMESTAMP + ") VALUES (?, ?, ?)";
    private final static String INSERT_DEPARTMENT = "INSERT INTO " + TABLE_DEPARTMENTS + "(" + COLUMN_DEPARTMENT_NAME + ", " + COLUMN_DEPARTMENT_LINK + ", " + COLUMN_DEPARTMENT_CSS_SELECTOR + ") VALUES (?, ?, ?)";
    private final static String INSERT_COURSE_DEPARTMENT_CHAIN = "INSERT INTO " + TABLE_DEPARTMENTS_COURSES + "( " + COLUMN_DEPARTMENT_COURSE_FK_COURSES + ", " + COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS + " ) VALUES(?, ?)";
    private final static String GET_COUESE_ID = "SELECT " + COLUMN_COURSE_ID + " FROM " + TABLE_COURSES + " WHERE " + COLUMN_COURSE_NAME + " = ?";
    private final static String GET_DEPARTMENT_ID = "SELECT " + COLUMN_DEPARTMENT_ID + " FROM " + TABLE_DEPARTMENTS + " WHERE " + COLUMN_DEPARTMENT_NAME + " = ?";


    public static void init() throws Exception
    {
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            db.executeStatement(CREATE_TABLE_DEPARTMENRS);
            db.executeStatement(CREATE_TABLE_DEPARTMENTS_COURSES);
            db.executeStatement(CREATE_TABLE_COURSES);
            db.executeStatement(CREATE_TABLE_USERS);
            db.executeStatement(CREATE_TABLE_ALIASES);
            db.executeStatement(CREATE_TABLE_USERS_COURSES);
            db.executeStatement(CREATE_TABLE_FEED);

            insertCourse(Course.GENERIC_COURSE_NAME, Arrays.asList(Course.GENERIC_COURSE_NAME));

            for (Course course : CourseAliasReader.getAliases())
            {
                insertCourse(course.getName(), course.getAliases());
                insertCourseDepartmentChain(course);
            }
        }
        finally
        {
            Database.close(db);
        }
    }


    private static void insertCourseDepartmentChain(Course course) throws Exception
    {
        Database db = null;
        ResultSet rs = null;
        try
        {
            db = Database.fromConnectionPool();

            for (Department department : course.getDepartment())
            {
                int departmentId = 0;
                int courseId = 0;

                rs = db.executeQuery(GET_DEPARTMENT_ID, department.getName());
                if (rs.next()) departmentId = rs.getInt(COLUMN_DEPARTMENT_ID);

                rs = db.executeQuery(GET_COUESE_ID, course.getName());
                if (rs.next()) courseId = rs.getInt(COLUMN_COURSE_ID);

                if (departmentId == 0 && courseId == 0) throw new Exception("Error retriving ids");
                db.executeStatement(INSERT_COURSE_DEPARTMENT_CHAIN, courseId, departmentId);
            }

        }
        finally
        {
            Database.close(db);
        }
    }


    public static void insertFeed(Feed feed) throws Exception
    {
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            db.executeStatement(INSERT_FEED, feed.getBody(), feed.getCourse().getId(), feed.getTimeStamp());
        }
        finally
        {
            Database.close(db);
        }
    }


    public static void insertCourse(String officialName, Collection<String> aliases) throws Exception
    {
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_COURSES_COLOURS_AND_NAMES);
            HashSet<Integer> colours = new HashSet<Integer>();

            officialName = officialName.toUpperCase();
            while (rs.next())
            {
                if (rs.getString(COLUMN_COURSE_NAME).equals(officialName)) return;
                colours.add(rs.getInt(COLUMN_COURSE_COLOUR));
            }

            boolean isInContrast = false;
            Integer generatedColour = 0;

            do
            {
                generatedColour = Course.generateRandomColor();
                if (ColourUtils.areInContrast(generatedColour, colours)) isInContrast = true;
            }
            while (!isInContrast);

            Long course_id = db.executeInsert(INSERT_COURSE, generatedColour, officialName);

            for (String alias : aliases)
            {
                db.executeStatement(INSERT_ALIAS, alias, course_id);
            }
        }
        finally
        {
            Database.close(db);
        }
    }


    public static List<Feed> insertFeeds(List<Feed> feeds) throws Exception
    {
        Set<Feed> storedFeeds = new HashSet<Feed>(getFeeds());

        List<Feed> newFeeds = new ArrayList<Feed>();
        for (Feed feed : feeds)
        {
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
                insertFeed(feed);
                newFeeds.add(feed);
            }
        }

        return newFeeds;
    }


    public static List<Department> getDepartments() throws Exception
    {
        List<Department> deparmentNames = null;
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_DEPARTMENTS);
            deparmentNames = new ArrayList<Department>();

            while (rs.next())
            {
                deparmentNames.add(new Department(rs.getString(COLUMN_DEPARTMENT_NAME), rs.getString(COLUMN_DEPARTMENT_LINK), rs.getString(COLUMN_DEPARTMENT_CSS_SELECTOR)));
            }

            return deparmentNames;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static Set<String> getAliases(String courseName) throws Exception
    {
        Set<String> aliases = null;
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_ALIASES_BY_COURSE_NAME, courseName);
            aliases = new HashSet<String>();

            while (rs.next())
            {
                aliases.add(rs.getString(COLUMN_ALIAS_VALUE));
            }

            return aliases;
        }
        finally
        {
            Database.close(db);
        }

    }


    public static List<Feed> getFeeds() throws Exception
    {
        List<Feed> feeds = null;
        Feed feed;
        Course course;
        Database db = null;
        Set<String> aliases = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_ALL_FEEDS);
            feeds = new ArrayList<Feed>();

            while (rs.next())
            {
                aliases = getAliases(rs.getString(COLUMN_COURSE_NAME));
                course = new Course(rs.getInt(COLUMN_COURSE_ID), rs.getString(COLUMN_COURSE_NAME), rs.getInt(COLUMN_COURSE_COLOUR), aliases);
                feed = new Feed(rs.getInt(COLUMN_FEED_ID), rs.getString(COLUMN_FEED_BODY), rs.getLong(COLUMN_FEED_TIMESTAMP), course);
                feeds.add(feed);
            }

            return feeds;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static List<Feed> getFeeds(String courseName) throws Exception
    {
        List<Feed> feeds = null;
        Feed feed;
        Course course;
        Database db = null;
        Set<String> aliases = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_FEEDS_BY_COURSE_NAME, courseName);
            feeds = new ArrayList<Feed>();

            while (rs.next())
            {
                aliases = getAliases(rs.getString(COLUMN_COURSE_NAME));
                course = new Course(rs.getInt(COLUMN_COURSE_ID), rs.getString(COLUMN_COURSE_NAME), rs.getInt(COLUMN_COURSE_COLOUR), aliases);
                feed = new Feed(rs.getInt(COLUMN_FEED_ID), rs.getString(COLUMN_FEED_BODY), rs.getLong(COLUMN_FEED_TIMESTAMP), course);
                feeds.add(feed);
            }

            return feeds;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static List<String> getDepartmentCourses(String departmentName) throws Exception
    {
        List<String> coursesNames = null;
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_COURSES_NAME_BY_DEPARTMENT_NAME, departmentName);
            coursesNames = new ArrayList<String>();

            while (rs.next())
            {
                coursesNames.add(rs.getString(COLUMN_COURSE_NAME));
            }

            return coursesNames;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static void signupUser(String token)
    {
        throw new TODOException("Te hai capi'");
    }


    public static List<Course> getAllCourses() throws Exception
    {
        List<Course> courses = null;
        Database db = null;
        Course course = null;
        Set<String> aliases = null;

        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_ALL_COURSES);
            courses = new ArrayList<Course>();

            while (rs.next())
            {
                aliases = getAliases(rs.getString(COLUMN_COURSE_NAME));
                course = new Course(rs.getInt(COLUMN_COURSE_ID), rs.getString(COLUMN_COURSE_NAME), rs.getInt(COLUMN_COURSE_COLOUR), aliases);
                courses.add(course);
            }

            return courses;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static List<Feed> getAllFeedsAfterIdForCourse(Long id, String courseName) throws Exception
    {
        List<Feed> feeds = null;
        Feed feed;
        Course course;
        Database db = null;
        Set<String> aliases = null;
        try
        {
            db = Database.fromConnectionPool();
            ResultSet rs = db.executeQuery(GET_ALL_FEEDS_AFTER_ID_BY_COURSE_NAME, courseName, id);
            feeds = new ArrayList<Feed>();

            while (rs.next())
            {
                aliases = getAliases(rs.getString(COLUMN_COURSE_NAME));
                course = new Course(rs.getInt(COLUMN_COURSE_ID), rs.getString(COLUMN_COURSE_NAME), rs.getInt(COLUMN_COURSE_COLOUR), aliases);
                feed = new Feed(rs.getInt(COLUMN_FEED_ID), rs.getString(COLUMN_FEED_BODY), rs.getLong(COLUMN_FEED_TIMESTAMP), course);
                feeds.add(feed);
            }

            return feeds;
        }
        finally
        {
            Database.close(db);
        }
    }


    public static List<Course> getCoursesOfDepartment(String department)
    {
        throw new TODOException("ti do il dipartimento, tu mi dai i corsi che fanno parte di quel dipartimento");
    }


    private static void insertDepartment(String departmentName, String link, String CSSSelector) throws Exception
    {
        Database db = null;
        try
        {
            db = Database.fromConnectionPool();
            db.executeStatement(INSERT_DEPARTMENT, departmentName, link, CSSSelector);
        }
        finally
        {
            if (db != null) db.close();
        }
    }


    public static void insertDepartments(List<Department> departments) throws Exception
    {
        for (Department department : departments)
            insertDepartment(department.getName(), department.getLink(), department.getCSSSelector());
    }


    public static void main(String[] argv) throws Exception
    {
        init();
        final File f = new File("resources/departments.txt");
        FileInputStream fileInputStream = new FileInputStream(f);
        List<Department> departments = new ArrayList<Department>();
        for (String department : StreamUtils.readLines(fileInputStream))
        {
            if (department.replace(" ", "").isEmpty()) continue;
            int separatorPosition = department.indexOf('|');
            String name = department.substring(0, separatorPosition);
            String link = department.substring(separatorPosition + 1, department.length());
            departments.add(new Department(name, "prova", link));
        }
        insertDepartments(departments);
    }
}
