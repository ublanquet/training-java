package main.java.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Utils {
    private static Logger logger = LoggerFactory.getLogger("main.java.dao.utils");
    private static final String url = "jdbc:mysql://localhost:3306/computer-database-db?useSSL=false";

    private static final String user = "root";

    private static final String pass = "pass";

    public static Connection getConnection(Connection connect){
        try {
            if(connect == null || connect.isClosed()) {

                connect = DriverManager.getConnection(url, user, pass);
                logger.debug("Getting connection");
            }
        }catch (SQLException e) {
            logger.error("Error getting connection" + e.getMessage() + e.getSQLState() + e.getStackTrace());
        }
        return connect;
    }

    public static long getGeneratedKey(Statement p){
        long generatedKey = 0;
        try {
            ResultSet rs = p.getGeneratedKeys();
            rs.next();
            generatedKey = rs.getLong(1);
        }catch(SQLException e){
            logger.error("Error retrieving generated key " + e.getMessage() + e.getSQLState() + e.getStackTrace() );
        }
        return generatedKey;
    }
}
