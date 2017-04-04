package services;

import persistance.model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Validate {
  private static Logger logger = LoggerFactory.getLogger("services.Validate");

  /**
   * check valid string to Long.
   *
   * @param input input string
   * @return is valid Long ?
   */
  public static Boolean isValidLong(String input) {
    Long longInput;
    try {
      longInput = Long.parseLong(input);
    } catch (NumberFormatException ex) {
      logger.error("Long validation error, input : " + input);
      return false;
    }

    return true;
  }

  /**
   * parse a Long from string.
   *
   * @param input input
   * @return converted number or null
   */
  public static Long parseLong(String input) {
    Long longInput = null;
    try {
      longInput = Long.parseLong(input);
    } catch (NumberFormatException ex) {
      logger.error("Long parsing error, input : " + input);
    }

    return longInput;
  }

  /**
   * parse a Long from string.
   *
   * @param input input
   * @return converted number or null
   */
  public static Boolean validOrder(String input) {
    switch (input) {
      case "order_company.name":
        return true;
      case "order_computer.name":
        return true;
      case "order_discontinued":
        return true;
      case "order_introduced":
        return true;
      default:
        return false;
    }
  }

  /**
   * parse a date, format dd/MM/yyyy.
   *
   * @param input input string
   * @return date obj or null if invalid
   */
  public static LocalDateTime parseDate(String input) {
    LocalDateTime date = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    try {
      if (!Objects.equals(input, "0") && !Objects.equals(input, "")) {
        date = LocalDate.parse(input, formatter).atStartOfDay();
        if (!isValidTimestamp(date)) {
          throw new DateTimeException("Invalid timestamp");
        }
      }
    } catch (DateTimeException ex) {
      logger.error("Date validation error, check dates " + ex.getMessage());
    } catch (NullPointerException ex) {
      logger.error("Date validation error, null input ");
    }

    return date;
  }

  /**
   * check if a date is valid.
   *
   * @param input input
   * @return true/false
   */
  public static Boolean isValidDate(String input) {
    LocalDateTime date = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      date = LocalDate.parse(input, formatter).atStartOfDay();
    } catch (DateTimeException ex) {
      logger.error("Date validation error, check input :" + input + ex.getMessage());
      return false;
    }

    return true;
  }

  /**
   * check if a date is valid.
   *
   * @param input input
   * @return true/false
   */
  public static Boolean isValidTimestamp(LocalDateTime input) {
    Timestamp ts;
    try {
      ts = Timestamp.valueOf(input);
    } catch (Exception ex) {
      logger.error("Date timestamp validation error, check input : " + input + ex.getMessage());
      return false;
    }

    return true;
  }

  /**
   * Validate a computer obj to make sure it's OK before persit.
   * @param c computer to validate
   * @return bool
   */
  public static Boolean isValidComputer(Computer c) {
    try {
      if (c == null) {
        return false;
      }
      if (c.getIntroduced() != null && !Validate.isValidTimestamp(c.getIntroduced())) {
        logger.error("Computer obj validation error : Date incorrect timestamp  : " + c.getIntroduced());
        return false;
      }
      if (c.getDiscontinued() != null && !Validate.isValidTimestamp(c.getDiscontinued())) {
        logger.error("Computer obj validation error : Date incorrect timestamp  : " + c.getDiscontinued());
        return false;
      }
      if (c.getIntroduced() != null && c.getDiscontinued() != null && c.getIntroduced().isAfter(c.getDiscontinued())) {
        logger.error("Computer obj validation error : date intro after date disco : " + c.getIntroduced() + "--" + c.getDiscontinued());
        return false;
      }
      if (c.getName() == null || c.getName().length() < 2) {
        logger.error("Computer obj validation error : name" + c.getName());
        return false;
      }
      if ((c.getCompany() != null && c.getCompanyId() != null) && c.getCompany().getId() == c.getCompanyId()) {
        logger.error("Computer obj validation error : mismatch companyID and company obj");
        return false;
      }
    } catch (Exception ex) {
      logger.error("Object validation error : " + c.toString() + ex.getMessage());
      return false;
    }

    return true;
  }
}
