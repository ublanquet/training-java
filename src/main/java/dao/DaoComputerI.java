package dao;

import  model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public interface DaoComputerI {
    String url = "jdbc:mysql://localhost:3306/computer-database-db?useSSL=false";

    String user = "root";

    String pass = "pass";

    Logger logger = LoggerFactory.getLogger(" dao.DaoComputer");

    static DaoComputer getInstance(){
        return DaoComputer.INSTANCE;
    }

    long create(Computer c);
    void update(Computer c);
    ArrayList<Computer> selectAll(long min, long max);
    Computer getById(long id);
    void delete(long id);
}
