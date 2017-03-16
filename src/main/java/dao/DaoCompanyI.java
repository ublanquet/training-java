package dao;


import main.java.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DaoCompanyI {

    String url = "jdbc:mysql://localhost:3306/computer-database-db?useSSL=false";

    String user = "root";

    String pass = "pass";

    Logger logger = LoggerFactory.getLogger("main.java.dao.DaoCompany");

    static DaoCompany getInstance(){
        return DaoCompany.INSTANCE;
    }

    long create(Company c);
    void update(Company c);
    ArrayList<Company> selectAll(long min, long max);
    Company getById(long id);
    void delete(long id);


}