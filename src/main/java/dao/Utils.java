package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
  private static final String CONFIGFILE = "/db.properties";
  private static final HikariConfig CFG = new HikariConfig(CONFIGFILE);
  private static final HikariDataSource DS = new HikariDataSource(CFG);
  private static Logger logger = LoggerFactory.getLogger("dao.utils");


  /**
   * get connection obj.
   * @return conected connection obj
   */
  public static Connection getConnection() {
    try {
      logger.debug("Getting connection");
      return DS.getConnection();
    } catch (SQLException e) {
      logger.error("Error getting connection" + e.getMessage() + e.getSQLState() + e.getStackTrace());
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
