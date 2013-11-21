package it.unitn.hci.feed;

import java.util.List;
import java.util.Map.Entry;
import it.unitn.hci.feed.common.models.Feed;
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

    private final static String CREATE_TABLE_DEPARTMENRS = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENTS + " ( " + COLUMN_DEPARTMENT_ID + " PRIMARY KEY, " + COLUMN_DEPARTMENT_NAME + " VARCHAR(200))";
    private final static String CREATE_TABLE_DEPARTMENTS_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENTS_COURSES + " ( " + COLUMN_DEPARTMENT_COURSE_ID + " PRIMARY KEY, " + COLUMN_DEPARTMENT_COURSE_FK_COURSES + " INTEGER REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "), " + COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS + " INTEGER REFERENCES " + TABLE_DEPARTMENTS + "(" + COLUMN_DEPARTMENT_ID + "))";
    private final static String CREATE_TABLE_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " ( " + COLUMN_COURSE_ID + " PRIMARY KEY, " + COLUMN_COURSE_NAME + " VARCHAR(60), " + COLUMN_COURSE_COLOUR + " INTEGER UNIQUE)";
    private final static String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ( " + COLUMN_USER_ID + " PRIMARY KEY, " + COLUMN_USER_TOKEN + " VARCHAR(256))";
    private final static String CREATE_TABLE_ALIASES = "CREATE TABLE IF NOT EXISTS " + TABLE_ALIASES + " ( " + COLUMN_ALIAS_ID + " PRIMARY KEY, " + COLUMN_ALIAS_VALUE + " VARCHAR(70), " + COLUMN_ALIAS_FK_COURSE + " INTEGER REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))";
    private final static String CREATE_TABLE_USERS_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS_COURSES + " ( " + COLUMN_USER_COURSE_ID + " PRIMARY KEY, " + COLUMN_USER_COURSE_FK_COURSES + " INTEGER REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "), " + COLUMN_USER_COURSE_FK_USERS + " INTEGER REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
    private final static String CREATE_TABLE_FEED = "CREATE TABLE IF NOT EXISTS " + TABLE_FEEDS + " ( " + COLUMN_FEED_ID + " PRIMARY KEY, " + COLUMN_FEED_FK_COURSE + " REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + ")," + COLUMN_FEED_TIMESTAMP + "INTEGER, " + COLUMN_FEED_BODY + " TEXT)";

    public final static String INSERT_FEED = "INSERT INTO " + TABLE_FEEDS + "(" + COLUMN_FEED_ID + ", " + COLUMN_FEED_BODY + ", " + COLUMN_FEED_FK_COURSE + ", " + COLUMN_FEED_TIMESTAMP + ") VALUES (?, ?, ?, ?)";
    public final static String INSERT_COURSE = "INSERT INTO " + TABLE_COURSES + "(" + COLUMN_COURSE_COLOUR + ", " + COLUMN_COURSE_NAME + ") VALUES (?, ?)";
    public final static String GET_COURSES_COLOURS_AND_NAMES = "SELECT " + COLUMN_COURSE_COLOUR + ", " + COLUMN_COURSE_NAME + " FROM " + TABLE_COURSES;


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

            for (Entry<String, List<String>> course : CourseAliasReader.getAliases().entrySet())
                insertCourse(course.getKey(), course.getValue());
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
            db.executeStatement(INSERT_FEED, feed.getId(), feed.getBody(), feed.getSubject(), feed.getTimeStamp());
        }
        finally
        {
            Database.close(db);
        }
    }


    public static void insertCourse(String officialName, List<String> aliases) throws Exception
    {
        throw new TODOException("Colle inserisci anche gli alias, ora inserisce solo il corso");
        // Database db = null;
        // try
        // {
        // db = Database.fromConnectionPool();
        // ResultSet rs = db.executeQuery(GET_COURSES_COLOURS_AND_NAMES);
        // HashSet<Integer> colours = new HashSet<Integer>();
        //
        // officialName = officialName.toUpperCase();
        // while (rs.next())
        // {
        // if (rs.getString(COLUMN_COURSE_NAME).equals(officialName)) return;
        // colours.add(rs.getInt(COLUMN_COURSE_COLOUR));
        // }
        //
        // boolean isInContrast = false;
        // Integer generatedColour = 0;
        //
        // do
        // {
        // generatedColour = Course.generateRandomColor();
        // if (ColourUtils.areInContrast(generatedColour, colours)) isInContrast = true;
        // }
        // while (!isInContrast);
        //
        // db.executeStatement(INSERT_COURSE, generatedColour, officialName);
        // }
        // finally
        // {
        // Database.close(db);
        // }
    }


    public static List<Feed> insertFeeds(List<Feed> feeds)
    {
        throw new TODOException("ti passo una lista di feed, quelli che sono gia nel db li ignori, gli altri li inserisci e mi ritorni una lista di quelli che hai appena inserito");
    }


    public static List<String> getDepartments()
    {
        throw new TODOException("ti passo una un nulla, e tu me la dai... la lista di stringhe che sono i nomi dei dipartimenti dell'universita' di trento (TN-IT-UE-EURASIA-HEARTH-SOLAR_SISTEM-MILK_WAY_ANDROMEDA)");
    }


    public static List<Feed> getFeeds(String courseName)
    {
        throw new TODOException("Prende tutti i feed per quel corso");
    }


    public static List<String> getDepartmentCourses(String departmentName)
    {
        throw new TODOException("Il nome di un dipartimentoe tu mi dai il nome di tutti i suoi corsi....afrojack");
    }


    public static void signupUser(String token)
    {
        throw new TODOException("Te hai capi'");
    }


    public static void main(String[] argv) throws Exception
    {
        init();
    }

}
