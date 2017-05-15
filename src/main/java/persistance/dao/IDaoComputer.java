package persistance.dao;

import persistance.model.Computer;
import persistance.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public interface IDaoComputer {
    Logger LOGGER = LoggerFactory.getLogger("persistance.dao.DaoComputer");

    /**
     * create computer.
     * @param c computer
     * @return generated id
     */
    Long create(Computer c);

    /**
     * update.
     * @param c computer
     * @return nb affected rows, 0 fail, 1 success
     */
    int update(Computer c);

    /**
     * select all computers.
     * @param min offset
     * @param max nb to return
     * @return list
     */
    ArrayList<Computer> selectAll(Long min, Long max);

    /**
     * get computer by id.
     * @param id id
     * @return computer
     */
    Computer getById(Long id);

    /**
     * delete.
     * @param id id
     * @return nb affected rows, 0 fail, 1 success
     */
    int delete(Long id);

  /**
   * get computer list paged.
   *
   * @param page the page
   * @return the filled page
   */
    Page<Computer> selectPaginated(Page page);

  /**
   * get computer list paged and filtered by name.
   * @param name name filter
   * @param page the page
   * @return the filled page
   */
  Page<Computer> selectFiltered(Page page, String name);

}
