package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
    private static final String URL = "jdbc:mysql://localhost:3306/computer-database-db?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "pass";
    private static Logger logger = LoggerFactory.getLogger(" dao.utils");

    /**
     * get connection obj.
     * @param connect connection obj to fill
     * @return conected connection obj
     */
    public static Connection getConnection(Connection connect) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            logger.error("Error getting connection driver " + e.getMessage() + e.getStackTrace());
        }
        try {
            if (connect == null || connect.isClosed()) {

                connect = DriverManager.getConnection(URL, USER, PASS);
                logger.debug("Getting connection");
            }
        } catch (SQLException e) {
            logger.error("Error getting connection" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
        return connect;
    }

    /**
     * return generated ID.
     * @param p statement
     * @return generated ID
     */
    public static Long getGeneratedKey(Statement p) {
        long generatedKey = 0;
        try {
            ResultSet rs = p.getGeneratedKeys();
            rs.next();
            generatedKey = rs.getLong(1);
        } catch (SQLException e) {
            logger.error("Error retrieving generated key " + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
        return generatedKey;
    }
}
