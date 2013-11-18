package it.unitn.hci.feed;

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
    
    private final static String COLUMN_DEPARTMENT_NAME = "department_name";
    private final static String COLUMN_DEPARTMENT_ID = "department_id";
    private final static String COLUMN_DEPARTMENT_COURSE_ID = "departments_course_id";
    private final static String COLUMN_DEPARTMENT_COURSE_FK_DEPARTMENTS = "departments_course_fk_departments";
    private final static String COLUMN_DEPARTMENT_COURSE_FK_COURSES = "departments_course_fk_courses";
    private final static String COLUMN_COURSE_ID = "course_id";
    private final static String COLUMN_COURSE_NAME = "course_name";
    private final static String COLUMN_ALIAS_ID = "alias_id";
    private final static String COLUMN_ALIAS_VALUE = "alias_value";
    private final static String COLUMN_ALIAS_FK_COURSE = "alias_fk_course";
    private final static String COLUMN_USER_COURSE_ID = "user_course_id";
    private final static String COLUMN_USER_COURSE_FK_COURSES = "user_course_fk_courses";
    private final static String COLUMN_USER_COURSE_FK_USERS = "user_course_fk_users";
    private final static String COLUMN_USER_ID = "user_id";
    private final static String COLUMN_USER_TOKEN = "user_token";

    private final static String CREATE_TABLE_DEPARTMENRS = "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENTS + " ( " + COLUMN_DEPARTMENT_ID + " PRIMARY KEY, " + COLUMN_DEPARTMENT_NAME + " VARCHAR(200))";


}
