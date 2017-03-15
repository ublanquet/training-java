package main.java.view;

import main.java.dao.*;
import main.java.model.*;

import java.io.IOException;
import java.util.ArrayList;

public class Cli {


    public static void main(String [] args)
    {
        System.out.println("Welcome to ComputerDataBase CLI");

        DaoComputer daoC = new DaoComputer();
        Computer computer = daoC.getById(5);
        System.out.println(computer.toString());

        ArrayList<Computer> cList = daoC.selectAll(0, 1000);

        for (Computer c: cList) {
            System.out.println(c.toString());
        }

        try {
            System.in.read();
        }catch (IOException ex){System.out.println(ex.getMessage());}
    }


    public static void mainLoop
}
