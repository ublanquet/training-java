package main.java.view;

import main.java.dao.*;
import main.java.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Cli {
    private static DaoComputer daoC = new DaoComputer();
    private static DaoCompany daoComp = new DaoCompany();
    private static Logger logger = LoggerFactory.getLogger("main.java.dao.Cli");


    private static Scanner scanner = new Scanner(System.in);

    public static void main(String [] args)
    {
        System.out.println("Welcome to ComputerDataBase CLI");
        logger.debug("CLI start");

        while(true) {
            String command = waitCommand();
            System.out.println( execCommand(command) );
        }
    }


    public static String waitCommand(){
        System.out.println("Available commands (not case sensitive) : getComputerbyId, getAllComputer, getAllCompany, createComputer");
        System.out.println("Enter your command : ");
        String command = scanner.nextLine();
        return command;
    }

    public static String getInput(){
        String input = scanner.nextLine();
        return input;
    }

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
            default: result = "Invalid Command";
                break;
        }

        return result;
    }

    public static String displayAllCompanies(String start, String end){
        System.out.println("Displaying all companies stored in DB : ");

        try {
            ArrayList<Company> cList = daoComp.selectAll( Long.parseLong(start), Long.parseLong(end) );

            for (Company c : cList) {
                System.out.println(c.toString());
            }
        }catch(Exception ex){return "Command error "+ex.getMessage();}
        return "Command success";
    }

    public static String displayAllComputers (String start, String end) {
        System.out.println("Displaying all computers stored in DB : ");

        try {
            ArrayList<Computer> cList = daoC.selectAll( Long.parseLong(start), Long.parseLong(end) );

            for (Computer c : cList) {
                System.out.println(c.toString());
            }
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success";
    }

    public static String displayComputerbyId(long id){
        System.out.println("Retrieving computer of ID " + id + ": ");

        try {
            Computer computer = daoC.getById(id);
            System.out.println(computer.toString());
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success";
    }

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
            LocalDateTime disco = null;

            if(!Objects.equals(input[startIndex + 2], "0")) {
                intro = LocalDateTime.parse(input[startIndex+2]);
            }
            if(!Objects.equals(input[startIndex + 3], "0")) {
                disco = LocalDateTime.parse(input[startIndex+3]);
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

    public static String createComputer(Computer c){
        if ( c == null ){
            return "Command error : error creating computer object, check args";
        }
        long generatedKey = 0;
        try {
            generatedKey = daoC.create(c);
            System.out.println(c.toString());
        } catch (Exception ex) {
            return "Command error "+ex.getMessage();
        }
        return "Command success, generated ID : " + generatedKey;
    }
}
