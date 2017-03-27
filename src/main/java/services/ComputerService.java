package services;

import dao.DaoComputer;
import dao.DaoComputerI;
import model.Computer;
import model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public enum ComputerService {
  INSTANCE;
  private Logger logger = LoggerFactory.getLogger(" services.ComputerService");

  private DaoComputer daoC = DaoComputerI.getInstance();


  public static ComputerService getInstance() {
    return ComputerService.INSTANCE;
  }

  /**
   * get by id.
   *
   * @param id id
   * @return computer
   */
  public Computer getById(Long id) {
    logger.info("Retrieving computer of ID " + id + ": ");
    Computer computer = null;
    try {
      computer = daoC.getById(id);
      logger.debug(computer.toString());
    } catch (Exception ex) {
      logger.error("Error retrieving computer" + ex.getMessage() + ex.getStackTrace());
    }
    return computer;
  }

  /**
   * Get count.
   *
   * @return count
   */
  public Long getCount() {
    logger.info("Retrieving computer count");
    long count = 0;
    try {
      count = daoC.getCount();
    } catch (Exception ex) {
      logger.error("Error retrieving computer" + ex.getMessage() + ex.getStackTrace());
    }
    return count;
  }

  /**
   * Get count of comp with name like param.
   *
   * @param name name
   * @return count
   */
  public Long getCount(String name) {
    logger.info("Retrieving computer count");
    if (name == null) {
      name = "";
    }
    long count = 0;
    try {
      count = daoC.getCount(name);
    } catch (Exception ex) {
      logger.error("Error retrieving computer" + ex.getMessage() + ex.getStackTrace());
    }
    return count;
  }

  /**
   * create in db.
   *
   * @param c compuetr
   * @return created id
   */
  public Long create(Computer c) {
    if (!Validate.isValidComputer(c)) {
      logger.error("Error persisting computer : invalid object");
      return null;
    }
    long generatedKey = 0;
    try {
      generatedKey = daoC.create(c);
    } catch (Exception ex) {
      logger.error("Error persisting computer " + ex.getMessage());
    }
    if (generatedKey == 0) {
      logger.error("Error persisting computer");
      return generatedKey;
    }

    logger.debug("Computer persist success, generated ID : " + generatedKey);
    return generatedKey;
  }

  /**
   * get all computers.
   *
   * @param start offset
   * @param end   nb
   * @return list computers
   */
  public ArrayList<Computer> getAll(Long start, Long end) {
    logger.debug("Retrieving all computers stored in DB : ");
    ArrayList<Computer> cList = new ArrayList<>();
    try {
      cList = daoC.selectAll(start, end);
    } catch (Exception ex) {
      logger.error("Error retrieving computers" + ex.getMessage());
    }
    return cList;
  }

  /**
   * get page compuetr.
   *
   * @param page page
   * @return filled page
   */
  public Page<Computer> getPaginated(Page<Computer> page) {
    logger.debug("Retrieving pagniated computers stored in DB : ");
    page = daoC.selectPaginated(page);

    return page;
  }

  /**
   * get filtered page compuetr.
   *
   * @param name name filter
   * @param page page
   * @return filled page
   */
  public Page<Computer> getFiltered(Page<Computer> page, String name) {
    logger.debug("Retrieving pagniated computers stored in DB : ");
    page = daoC.selectFiltered(page, name != null ? name : "");

    return page;
  }

  /**
   * create computer obj from string array.
   *
   * @param input input
   * @return computer obj
   */
  public Computer createComputerObjectfromArray(String[] input) {
    Computer c = null;
    if (input.length > 4 || input.length == 0) {
      logger.error("Computer object creation error, wrong input array, length " + input.length + " expected 4");
      return c;
    }

    try {
      Long companyId = Long.parseLong(input[0]);
      String name = input[1];

      LocalDateTime intro = null;
      LocalDateTime disco = null;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


      if (!Objects.equals(input[2], "0")) {
        intro = LocalDate.parse(input[2], formatter).atStartOfDay();
      }
      if (!Objects.equals(input[3], "0")) {
        disco = LocalDate.parse(input[2], formatter).atStartOfDay();
      }

      c = new Computer(companyId, name, intro, disco);
      logger.debug("computer object created" + c.toString());
    } catch (DateTimeException ex) {
      logger.error("Computer object creation error, check dates " + ex.getMessage());
    } catch (Exception ex) {
      logger.error("Computer object creation error " + ex.getMessage());
    }
    return c;
  }

  /**
   * delete by id.
   * @param id id to delete
   * @return nb affected rows, 0 fail, 1 success
   */
  public int delete(long id) {
    logger.debug("deleting computer of id : " + id);
    int affectedRow = daoC.delete(id);
    if (affectedRow == 0) {
      logger.error("No Computer deleted, incorrect id");
    }
    return affectedRow;
  }

  /**
   * delete by id.
   * @param c obj to delete
   * @return nb affected rows, 0 fail, 1 success
   */
  public int update(Computer c) {
    logger.debug("Updating computer of id : " + c.getId());
    if (!Validate.isValidComputer(c)) {
      logger.error("No Computer updated, incorrect object");
      return 0;
    }
    int affectedRow = daoC.update(c);
    if (affectedRow == 0) {
      logger.error("No Computer updated, error");
    }
    return affectedRow;
  }
}
