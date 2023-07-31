package io.github.mateuszlubian00.itemcompare.util;

import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
public class DBUtil {

    private static Connection dbConnection = null;
    private static final String connectionString = "jdbc:derby:memory:data;create=true";

    public static void dbConnect(){
        try {
            dbConnection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while establishing connection to database");
            System.exit(0);
        }
    }

    public static void dbDisconnect() {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while closing connection to database");
            System.exit(0);
        }
    }

    /** Database Execute Query Operation
     */
    public static ResultSet dbExecuteQuery(String query) {
        dbConnect();
        Statement statement = null;
        ResultSet resultSet = null;
        CachedRowSet cachedResult = null;
        try {
            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);

            // Avoid "Closed Connection" error by using CachedRowSet.
            cachedResult = RowSetProvider.newFactory().createCachedRowSet();
            cachedResult.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while trying to execute query " + query);
            System.exit(0);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                System.out.println("There has been a fatal error while trying to close result set " + resultSet);
                System.exit(0);
            }

            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("There has been a fatal error while trying to close statement " + statement);
                System.exit(0);
            }
            dbDisconnect();
        }
        return cachedResult;
    }

    /** Update/Insert/Delete operations
     */
    public static void dbExecuteUpdate(String sqlStatement) {
        dbConnect();
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
            statement.executeUpdate(sqlStatement);
        } catch (SQLException e) {
            System.out.println("There has been a fatal error while trying to execute statement " + sqlStatement);
            System.exit(0);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("There has been a fatal error while trying to close statement " + statement);
                System.exit(0);
            }
            dbDisconnect();
        }
    }
}
