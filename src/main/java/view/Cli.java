package view;

import dao.DaoCompany;
import dao.DaoCompanyI;
import dao.DaoComputer;
import dao.DaoComputerI;

import model.Company;
import model.Computer;
import model.Page;
import  services.CompanyService;
import  services.ComputerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Cli {
    private static DaoComputer daoC = DaoComputerI.getInstance();
    private static DaoCompany daoComp = DaoCompanyI.getInstance();
    private static Logger logger = LoggerFactory.getLogger(" dao.Cli");
    private static Boolean running = true;
    private static ComputerService compService = new ComputerService();
    private static CompanyService companyService = new CompanyService();

    private static Scanner scanner = new Scanner(System.in);

    private static Page<Computer> pageComputer = new Page<Computer>(20, 0);
    private static Page<Company> pageCompany = new Page<Company>(20, 0);


    public static void main(String [] args)
    {
        System.out.println("Welcome to ComputerDataBase CLI");
        logger.debug("CLI start");


        while(running) {
            String command = waitCommand();
            System.out.println( execCommand(command) );
        }
    }

    /**
     * display available commands and return user input command
     * @return
     */
    public static String waitCommand(){
        System.out.println("Available commands (not case sensitive) : getComputerbyId, getAllComputer, getAllCompany, createComputer, getallcomputerp, quit");
        System.out.println("Enter your command : ");
        String command = scanner.nextLine();
        return command;
    }

    /**
     * get input
     * @return
     */
    public static String getInput(){
        String input = scanner.nextLine();
        return input;
    }

    /**
     * convert input to long
     * @param input
     * @return
     */
    public static long getLongInput(String input){
        long longInput = -1;
        try{
            longInput = Long.parseLong(input);
        }catch (NumberFormatException ex){System.out.println("Wrong input, please enter a positive integrer number");}
        if(longInput < 0) {
            longInput = getLongInput(getInput());
        }
        return longInput;
    }

    /**
     * controller switch to execute commands
     * @param command
     * @return
     */
    public static String execCommand(String command){
        command = command.toLowerCase();
        String result = "";
        String[] splited = new String[0];
        if(command.contains(" ")) {
            splited = command.split("\\s+");
            command = splited[0];
        }

        switch(command){
            case "getallcomputer":
                if(splited.length > 1) {
                    result = displayAllComputers(splited[1], splited[2]);
                }else{
                    result = displayAllComputers("1","1000");
                }
                break;
            case "getallcompany":
                if (splited.length > 1) {
                    result = displayAllCompanies(splited[1], splited[2]);
                } else {
                    result = displayAllCompanies("1","1000");
                }
                break;
            case "getcomputerbyid":
                if(splited.length > 0){
                    result = displayComputerbyId( getLongInput(splited[1]) );
                }else {
                    System.out.println("Enter the computer ID to retrieve");
                    long id = getLongInput(getInput());
                    result = displayComputerbyId(id);
                }
                break;
            case "createcomputer":
                if(splited.length > 0){
                    result = createComputer( createComputerObjectfromArray(splited) );
                }else {
                    System.out.println("Enter the computer to create under format : 'companyId name intro disco' the intro and disco dates can be 0");
                    result = createComputer( createComputerObject(getInput()) );
                }
                break;
            case "getallcomputerp":
                if (splited.length > 2) {
                    result = displayAllComputerPaged(splited[1], splited[2]);
                } else if(splited.length == 2){
                    result = displayAllComputerPaged(splited[1],"20");
                }
                else{
                    result = displayAllComputerPaged("0","20");
                }
                break;
            case "quit":
                running = false;
                break;
            default: result = "Invalid Command";
                break;
        }

        return result;
    }

    /**
     * display all companies
     * @param start starting index
     * @param end nb of entries
     * @return
     */
    public static String displayAllCompanies(String start, String end){
        System.out.println("Displaying all companies stored in DB : ");

        try {
            ArrayList<Company> cList = companyService.getAllCompany(Long.parseLong(start), Long.parseLong(end));
            for (Company c : cList) {
                System.out.println(c.toString());
            }
        }catch(Exception ex){return "Command error "+ex.getMessage();}
        return "Command success";
    }

    /**
     * display computer with pagination
     * @param pageN
     * @param nb
     * @return
     */
    public static String displayAllComputerPaged(String pageN, String nb){
        System.out.println("Displaying all computers stored in DB : ");
        try {
            pageComputer.setNbEntries(Integer.parseInt(nb));
            pageComputer.setCurrentPage(Integer.parseInt(pageN));
            compService.getPaginatedComputers(pageComputer);

            System.out.println(pageComputer.toString());
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success";
    }

    /**
     * display all computer
     * @param start offset
     * @param end nb to return
     * @return
     */
    public static String displayAllComputers (String start, String end) {
        System.out.println("Displaying all computers stored in DB : ");

        try {
            ArrayList<Computer> cList = compService.getAllComputers(Long.parseLong(start), Long.parseLong(end) );

            for (Computer c : cList) {
                System.out.println(c.toString());
            }
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success";
    }

    /**
     * display computer by id
     * @param id
     * @return
     */
    public static String displayComputerbyId(long id){
        System.out.println("Retrieving computer of ID " + id + ": ");
        try {
            Computer computer = compService.getComputerbyId(id);//daoC.getById(id);

            System.out.println(computer.toString());
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success";
    }

    /**
     * create a computer obj from input string
     * @param input
     * @return
     */
    public static Computer createComputerObject(String input){
        Computer c = null;

        String[] splited = new String[0];
        if(input.contains(" ")) {
            splited = input.split("\\s+");
        }
        if(splited.length == 0 || splited.length > 5){
            System.out.println("Wrong arg number");
            return c;
        }
        c = createComputerObjectfromArray(splited);

        return c;
    }

    /**
     * create computer obj for an array of strings
     * @param input
     * @return
     */
    public static Computer createComputerObjectfromArray(String[] input){
        Computer c = null;
        int startIndex = 0;

        if(input.length > 4) {
            startIndex = 1;
        }

        try {
            long companyId = Long.parseLong(input[startIndex]);
            String name = input[startIndex+1];

            LocalDateTime intro = null;
            LocalDateTime disco = null ;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


            if(!Objects.equals(input[startIndex + 2], "0")) {
                intro = LocalDate.parse(input[startIndex+2], formatter) .atStartOfDay();
            }
            if(!Objects.equals(input[startIndex + 3], "0")) {
                disco = LocalDate.parse(input[startIndex+3], formatter) .atStartOfDay();
            }

            c = new Computer(companyId, name, intro, disco);
            System.out.println(c.toString());
        } catch (DateTimeException ex) {
            System.out.println("Command error, check dates "+ex.getMessage());throw ex;
        } catch (Exception ex) {
            System.out.println("Command error "+ex.getMessage());
        }
        return c;
    }

    /**
     * create computer in db
     * @param c
     * @return
     */
    public static String createComputer(Computer c){
        if ( c == null ){
            return "Command error : error creating computer object, check args";
        }
        long generatedKey = 0;
        try {
            generatedKey = compService.createComputer(c);
            System.out.println(c.toString());
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success, generated ID : " + generatedKey;
    }
}
