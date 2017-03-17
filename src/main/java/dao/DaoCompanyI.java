package dao;


import model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public interface DaoCompanyI {

    Logger LOGGER = LoggerFactory.getLogger(" dao.DaoCompany");

    /**
     * reutrn unique instance.
     * @return instance
     */
    static DaoCompany getInstance() {
        return DaoCompany.INSTANCE;
    }

    /**
     * create a company in db.
     * @param c company
     * @return generated id
     */
    long create(Company c);

    /**
     * update.
     * @param c company
     */
    void update(Company c);

    /**
     * select all.
     * @param min offset
     * @param max number to get
     * @return list company
     */
    ArrayList<Company> selectAll(long min, long max);

    /**
     * get by id.
     * @param id id.
     * @return company
     */
    Company getById(long id);

    /**
     * delete.
     * @param id id
     */
    void delete(long id);


}