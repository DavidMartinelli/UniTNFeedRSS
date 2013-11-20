package it.unitn.hci.feed;

import java.sql.ResultSet;
import java.util.HashSet;

import it.unitn.hci.feed.common.models.Course;
import it.unitn.hci.feed.common.models.Feed;

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
    private final static String CREATE_TABLE_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " ( " +  COLUMN_COURSE_ID + " PRIMARY KEY, " + COLUMN_COURSE_NAME  + " VARCHAR(60), "+ COLUMN_COURSE_COLOUR + " INTEGER UNIQUE)";
    private final static String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ( " + COLUMN_USER_ID + " PRIMARY KEY, " + COLUMN_USER_TOKEN + " VARCHAR(256))"; 
    private final static String CREATE_TABLE_ALIASES = "CREATE TABLE IF NOT EXISTS " + TABLE_ALIASES + " ( " + COLUMN_ALIAS_ID + " PRIMARY KEY, " + COLUMN_ALIAS_VALUE + " VARCHAR(70), " + COLUMN_ALIAS_FK_COURSE + " INTEGER REFERENCES " + TABLE_COURSES + "(" + COLUMN_COURSE_ID + "))"; 
    private final static String CREATE_TABLE_USERS_COURSES = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS_COURSES + " ( " + COLUMN_USER_COURSE_ID + " PRIMARY KEY, " + COLUMN_USER_COURSE_FK_COURSES + " INTEGER REFERENCES " + TABLE_COURSES + "("  + COLUMN_COURSE_ID + "), " + COLUMN_USER_COURSE_FK_USERS + " INTEGER REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
    private final static String CREATE_TABLE_FEED = "CREATE TABLE IF NOT EXISTS " + TABLE_FEEDS + " ( " + COLUMN_FEED_ID + " PRIMARY KEY, " + COLUMN_FEED_FK_COURSE + " REFERENCES " + TABLE_COURSES + "(" +  COLUMN_COURSE_ID + ")," + COLUMN_FEED_TIMESTAMP + "INTEGER, " + COLUMN_FEED_BODY + " TEXT)"; 
    		
    public final static String INSERT_FEED = "INSERT INTO " + TABLE_FEEDS + "(" + COLUMN_FEED_ID + ", " + COLUMN_FEED_BODY + ", " + COLUMN_FEED_FK_COURSE + ", " + COLUMN_FEED_TIMESTAMP + ") VALUES (?, ?, ?, ?)";
    public final static String INSERT_COURSE = "INSERT INTO " + TABLE_COURSES + "(" + COLUMN_COURSE_COLOUR + ", " + COLUMN_COURSE_NAME + ") VALUES (?, ?)";
    public final static String GET_COURSES_COLOURS = "SELECT " + COLUMN_COURSE_COLOUR + " FROM " + TABLE_COURSES;
    
    public static void init() throws Exception
    {
    	Database db = null;
    	try{
    		db = Database.fromConnectionPool();
    		db.executeStatement(CREATE_TABLE_DEPARTMENRS);
    		db.executeStatement(CREATE_TABLE_DEPARTMENTS_COURSES);
    		db.executeStatement(CREATE_TABLE_COURSES);
    		db.executeStatement(CREATE_TABLE_USERS);
    		db.executeStatement(CREATE_TABLE_ALIASES);
    		db.executeStatement(CREATE_TABLE_USERS_COURSES);
    		db.executeStatement(CREATE_TABLE_FEED);
    	}
    	finally
    	{
    		Database.close(db);
    	}
    }
    
    public static void main(String[] argv){
    	Database db = null;
    	try{
        	db = Database.fromConnectionPool();
    		init();
    	} 
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		Database.close(db);
    	}
    }
    
    public static void insertFeed(Feed feed) throws Exception {
    	Database db = null;
    	try{
        	db = Database.fromConnectionPool();
    		db.executeStatement(INSERT_FEED, feed.getId(), feed.getBody(), feed.getSubject(), feed.getTimeStamp());
    	} 
    	finally
    	{
    		Database.close(db);
    	}
    }
    
    public static void insertCourse(String coursename) throws Exception {
    	Database db = null;
    	try{
        	db = Database.fromConnectionPool();
        	ResultSet rs = db.executeQuery(GET_COURSES_COLOURS);
        	HashSet<Integer> colours = new HashSet<Integer>();
        	
        	while(rs.next()){
        		colours.add(rs.getInt(COLUMN_COURSE_COLOUR));
        	}
        	
        	boolean inContrast = false;
        	Integer generatedColour = 0;
        	while(!inContrast){
        		generatedColour = Course.generateRandomColor();
        		//inContrast //=jan method 
        	}
    		db.executeStatement(INSERT_COURSE, generatedColour, coursename);
    	} 
    	finally
    	{
    		Database.close(db);
    	}
    }
    

}
