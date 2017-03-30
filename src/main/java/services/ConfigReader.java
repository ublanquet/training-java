package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
  private static Logger logger = LoggerFactory.getLogger("services.ConfigReader");
  private static Properties prop;

  /**
   * Read prop file named "config.properties".
   */
  public static void readProperties() {
    prop = new Properties();
    InputStream input = null;
    try {
      //static path method
      //logger.debug("Looking for config file at " + System.getProperty("user.dir"));
      logger.debug("Looking for config file at /opt/config.properties");
      input = new FileInputStream("/opt/config.properties");

      //in webcontent
      //input = context.getResourceAsStream("/WEB-INF/foo.properties"); //need inject context

      //java package struct
      //ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      //input = classLoader.getResourceAsStream("resources/config.properties");
      // load a properties file
      prop.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
      logger.error("Error reading properties" + ex.getMessage());
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
          logger.error("Error reading properties");
        }
      }
    }

  }

  /**
   * get property from key.
   * @param key key
   * @return val
   */
  public static String getProperty(String key) {
    if (prop == null) {
      readProperties();
    }
    return prop.getProperty(key);
  }
}
