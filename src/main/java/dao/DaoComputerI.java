package dao;

import model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public interface DaoComputerI {
    Logger LOGGER = LoggerFactory.getLogger(" dao.DaoComputer");

    /**
     * get unique instance.
     * @return instance
     */
    static DaoComputer getInstance() {
        return DaoComputer.INSTANCE;
    }

    /**
     * create computer.
     * @param c computer
     * @return generated id
     */
    long create(Computer c);

    /**
     * update.
     * @param c computer
     */
    void update(Computer c);

    /**
     * select all computers.
     * @param min offset
     * @param max nb to return
     * @return list
     */
    ArrayList<Computer> selectAll(long min, long max);

    /**
     * get computer by id.
     * @param id id
     * @return computer
     */
    Computer getById(long id);

    /**
     * delete.
     * @param id id
     */
    void delete(long id);
}
