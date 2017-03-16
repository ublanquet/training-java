package services;

import  dao.DaoComputer;
import  dao.DaoComputerI;
import  model.Computer;
import  model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class ComputerService {
    private Logger logger = LoggerFactory.getLogger(" services.ComputerService");

    private DaoComputer daoC = DaoComputerI.getInstance();

    public Computer getComputerbyId(long id){
        logger.info("Retrieving computer of ID " + id + ": ");
        Computer computer = null;
        try {
             computer = daoC.getById(id);
            logger.debug(computer.toString());
        } catch (Exception ex) {
            logger.error("Error retrieving computer" + ex.getMessage() + ex.getStackTrace() );
        }
        return computer;
    }

    public long createComputer(Computer c){
        if ( c == null ){
            logger.error("Error persisting computer : received null object" );
        }
        long generatedKey = 0;
        try {
            generatedKey = daoC.create(c);
        } catch (Exception ex) {
            logger.error("Error persisting computer "+ex.getMessage());
        }
        logger.debug("Computer persist success, generated ID : " + generatedKey);
        return generatedKey;
    }

    public ArrayList<Computer> getAllComputers (long start, long end) {
        logger.debug("Retrieving all computers stored in DB : ");
        ArrayList<Computer> cList = new ArrayList<>();
        try {
           cList = daoC.selectAll( start, end);
        } catch (Exception ex) {
            logger.error("Error retrieving computers" + ex.getMessage());
        }
        return cList;
    }

    public Page<Computer> getPaginatedComputers (Page<Computer> page) {
        logger.debug("Retrieving pagniated computers stored in DB : ");
        try {
            page = daoC.selectPaginated( page );
        } catch (Exception ex) {
            logger.error("Error retrieving computers" + ex.getMessage());
        }
        return page;
    }

    public Computer createComputerObjectfromArray(String[] input){
        Computer c = null;
        if( input.length > 4 || input.length == 0) {
            logger.error("Computer object creation error, wrong input array, length " + input.length + " expected 4");
            return c;
        }

        try {
            long companyId = Long.parseLong(input[0]);
            String name = input[1];

            LocalDateTime intro = null;
            LocalDateTime disco = null ;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


            if(!Objects.equals(input[2], "0")) {
                intro = LocalDate.parse(input[2], formatter) .atStartOfDay();
            }
            if(!Objects.equals(input[3], "0")) {
                disco = LocalDate.parse(input[2], formatter) .atStartOfDay();
            }

            c = new Computer(companyId, name, intro, disco);
            logger.debug("computer object created" + c.toString());
        } catch (DateTimeException ex) {
            logger.error("Computer object creation error, check dates "+ex.getMessage());
        } catch (Exception ex) {
            logger.error("Computer object creation error "+ex.getMessage());
        }
        return c;
    }
}
