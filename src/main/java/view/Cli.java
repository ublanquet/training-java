package main.java.view;

import main.java.dao.*;
import main.java.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Cli {
    private static DaoComputer daoC = new DaoComputer();

    public static void main(String [] args)
    {
        System.out.println("Welcome to ComputerDataBase CLI");

        while(true) {
            String command = waitCommand();
            System.out.println( execCommand(command) );
        }
    }


    public static String waitCommand(){
        System.out.println("Available commands : getComputerbyId, getAllComputer");
        System.out.println("Enter your command : ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        return command;
    }

    public static String getInput(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return input;
    }

    public static long getLongInput(String input){
        //Scanner scanner = new Scanner(System.in);
        //String input = scanner.nextLine();

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
        String result = "";
        String[] splited = new String[0];
        if(command.contains(" ")) {
            splited = command.split("\\s+");
            command = splited[0];
        }

        switch(command){
            case "":  ;
                break;
            case "getAllComputer": result = displayAllComputers();
                break;
            case "getComputerbyId":
                if(splited.length > 0){
                    result = displayComputerbyId( getLongInput(splited[1]) );
                }else {
                    System.out.println("Enter the computer ID to retrieve");
                    long id = getLongInput(getInput());
                    result = displayComputerbyId(id);
                }
                break;
            default: result = "Invalid Command";
                break;
        }

        return result;
    }

    public static String displayAllComputers(){
        System.out.println("Displaying all computers stored in DB : ");

        try {
            ArrayList<Computer> cList = daoC.selectAll(0, 1000);

            for (Computer c : cList) {
                System.out.println(c.toString());
            }
        }catch(Exception ex){return "Command error "+ex.getMessage();}
        return "Command success";
    }

    public static String displayComputerbyId(long id){
        System.out.println("Retrieving computer of ID " + id + ": ");

        try {
            Computer computer = daoC.getById(id);
            System.out.println(computer.toString());
        }catch(Exception ex){return "Command error "+ex.getMessage();}
        return "Command success";
    }
}
