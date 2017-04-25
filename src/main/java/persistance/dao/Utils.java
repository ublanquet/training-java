package persistance.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
  private static Logger logger = LoggerFactory.getLogger("persistance.dao.utils");

  private static DataSource ds;

  public static void setDS(DataSource ds) {
    Utils.ds = ds;
  }
  public static DataSource getDS() {
    return ds;
  }



  /**
   * get connection obj unique by thread.
   *
   * @return conected connection obj
   */
  public static Connection getConnection() {
    try {
      logger.debug("Getting connection");
      //return ds.getConnection();
      return DataSourceUtils.getConnection(ds); //use utils for transactionManager
    } catch (Exception e) {
      logger.error("Error getting connection" + e.getMessage() +  e.getStackTrace());
    }
    return null;
  }

  /**
   * return generated ID.
   *
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
