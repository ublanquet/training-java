package main.java.services;


import main.java.dao.DaoCompany;
import main.java.dao.DaoCompanyI;

import main.java.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CompanyService {

    private Logger logger = LoggerFactory.getLogger("main.java.services.CompanyService");

    private DaoCompany daoC = DaoCompanyI.getInstance();

    public ArrayList<Company> getAllCompany(long start, long end) {
        logger.debug("Displaying all companies stored in DB : ");
        ArrayList<Company> cList= null;
        try {
            cList = daoC.selectAll(start, end);
        } catch (Exception ex) {
            logger.error("All Company retrieval failure "+ex.getMessage());
        }
        return cList;
    }

}
