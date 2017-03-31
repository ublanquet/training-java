package services;


import persistance.dao.DaoCompany;
import persistance.dao.DaoCompanyI;
import persistance.dao.DaoComputer;
import persistance.dao.DaoComputerI;
import persistance.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CompanyService {

  private Logger logger = LoggerFactory.getLogger(" services.CompanyService");

  private DaoCompany daoC = DaoCompanyI.getInstance();
  private DaoComputer daoComputer = DaoComputerI.getInstance();


  /**
   * get all companies.
   * @param start offset
   * @param end nb
   * @return list companies
   */
  public ArrayList<Company> getAll(Long start, Long end) {
    logger.debug("Displaying all companies stored in DB : ");
    ArrayList<Company> cList = null;
    try {
      cList = daoC.selectAll(start, end);
    } catch (Exception ex) {
      logger.error("All Company retrieval failure " + ex.getMessage());
    }
    return cList;
  }

  /**
   * get all companies.
   * @return list companies
   */
  public ArrayList<Company> getAll() {
    logger.debug("Displaying all companies stored in DB : ");
    ArrayList<Company> cList = null;
    try {
      cList = daoC.selectAll();
    } catch (Exception ex) {
      logger.error("All Company retrieval failure " + ex.getMessage());
    }
    return cList;
  }


  /**
   * delete a company and all associated computers.
   * @param id id
   * @return deleted rows
   */
  public int delete(long id) {
    logger.debug("Deleting company of ID : " + id);
    try {
      daoC.startTransaction();
      int deletedComputer = daoComputer.deleteByCompanyId(id);
      daoC.delete(id);
      daoC.commitTransaction();
      return deletedComputer;
    } catch (Exception ex) {
      logger.error("Error deleting company of ID : " + id);
    }
    return 0;
  }

    /*public Page<Company> getPaginatedCompanies (Page<Company> page) {
        LOGGER.debug("Retrieving pagniated companies stored in DB : ");
        try {
            page = daoC.selectPaginated( page );
        } catch (Exception ex) {
            LOGGER.error("Error retrieving computers" + ex.getMessage());
        }
        return page;
    }*/

}
