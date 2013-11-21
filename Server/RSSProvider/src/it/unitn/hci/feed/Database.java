package it.unitn.hci.feed;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class Database
{

    private Connection connection;
    private final static String PATH = "database.db";
    private static final ThreadLocal<Database> connectionPool = new ThreadLocal<Database>();
    private static final Object LOCK = new Object();


    /**
     * Extracts a database connection from a fixed connection pool
     * 
     * @return a new database object constructed from the extracted connection
     * @throws Exception
     * @throws SQLException
     */
    public static Database fromConnectionPool() throws Exception
    {
        Database local = connectionPool.get();
        if (local == null || local.connection.isClosed())
        {
            local = createConnection(PATH);
        }
        return local;
    }


    /**
     * Executes a SQL command using the specified parameters. But, for now, the parameters in the statement is only "?", we put the real parameters into the query using the function 'addParamenters' that is a private methos called automatically in this method (executeStatement), this provide to avoid sql injection. Statement is close automatically in this method (executeStatement). (update, create)
     * 
     * @param query
     * @param parameters
     * @throws SQLException
     */
    public void executeStatement(String query, Object... parameters) throws SQLException
    {
        synchronized (LOCK)
        {
            PreparedStatement statement = connection.prepareStatement(query);
            try
            {
                addParameters(statement, parameters);
                statement.execute();
            }
            finally
            {
                statement.close();
            }
        }
    }


    /**
     * Executes a SQL command using the specified parameters. But, for now, the parameters in the statement is only "?", we put the real parameters into the query using the function 'addParamenters' that is a private methos called automatically in this method (executeStatement), this provide to avoid sql injection. Statement is close automatically in this method (executeStatement). (insert)
     * 
     * @param query
     * @param parameters
     * @return the generated id
     * @throws SQLException
     */
    public Long executeInsert(String query, Object... parameters) throws SQLException
    {
        synchronized (LOCK)
        {
            Long generatedKey = null;
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            try
            {
                addParameters(statement, parameters);
                statement.executeUpdate();

                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next())
                {
                    generatedKey = rs.getLong(1);
                }
                return generatedKey;
            }
            finally
            {
                statement.close();
            }
        }
    }


    /**
     * Executes a SQL query using the specified parameters. (query)
     * 
     * @param query
     * @param parameters
     * @return
     * @throws SQLException
     */
    public ResultSet executeQuery(String query, Object... parameters) throws SQLException
    {
        synchronized (LOCK)
        {
            PreparedStatement statement = connection.prepareStatement(query);
            addParameters(statement, parameters);
            return statement.executeQuery();
        }
    }


    /**
     * Closes a ResultSet and its Statement.
     * 
     * @param resultSet
     * @throws SQLException
     */
    public static void closeResultSet(ResultSet resultSet) throws SQLException
    {
        if (resultSet != null)
        {
            Statement statement = resultSet.getStatement();
            resultSet.close();
            statement.close();
        }
    }


    /**
     * It closes the database connection.
     * 
     * @throws SQLException
     */
    public void close() throws SQLException
    {
        connection.close();
    }


    /**
     * Closes the database connection in static way.
     * 
     * @throws SQLException
     */
    public static void close(Database db)
    {
        if (db != null)
        {
            try
            {
                db.close();
            }
            catch (SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * Creates a new database connection using the specified parameters.
     * 
     * @param host
     * @param dbName
     * @param user
     * @param password
     * @return
     * @throws SQLException
     */
    private static Database createConnection(String path) throws Exception
    {
        Class.forName("org.sqlite.JDBC");
        return new Database((Connection) DriverManager.getConnection("jdbc:sqlite:" + path));
    }


    /**
     * This method add the parameters into the string-query (already in the statement) In this mode we provide to avoid sql injection.
     * 
     * @param statement
     * @param parameters
     * @throws SQLException
     */
    private void addParameters(PreparedStatement statement, Object[] parameters) throws SQLException
    {
        if (parameters == null)
        {
            return;
        }
        else
        {
            for (int i = 0; i < parameters.length; i++)
            {
                Object value = parameters[i];
                if (value instanceof Date)
                {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    value = df.format(value);
                }
                statement.setObject(i + 1, value);
            }
        }
    }


    private Database(Connection connection)
    {
        this.connection = connection;
    }
}
