package io.github.mateuszlubian00.itemcompare.util;

import java.sql.*;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
public class DBUtil {

    private static Connection dbConnection = null;
    private static final String connectionString = "jdbc:derby:memory:data;create=true";

    public static void dbConnect() throws SQLException {
        dbConnection = DriverManager.getConnection(connectionString);
    }

    public static void dbDisconnect() throws SQLException {
        if (dbConnection != null && !dbConnection.isClosed()) {
            dbConnection.close();
        }
    }

    /** Database Execute Query Operation
     */
    public static ResultSet dbExecuteQuery(String query) throws SQLException {
        dbConnect();

        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Avoid "Closed Connection" error by using CachedRowSet.
        CachedRowSet cachedResult = RowSetProvider.newFactory().createCachedRowSet();
        cachedResult.populate(resultSet);

        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        dbDisconnect();

        return cachedResult;
    }

    /** Update/Insert/Delete operations
     */
    public static void dbExecuteUpdate(String sqlStatement) throws SQLException {
        dbConnect();

        Statement statement = dbConnection.createStatement();
        statement.executeUpdate(sqlStatement);

        if (statement != null) {
            statement.close();

        dbDisconnect();
        }
    }
}
