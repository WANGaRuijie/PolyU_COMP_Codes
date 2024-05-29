package model;

import model.instruction.system.LoginChoiceExecutor;
import model.instruction.system.QuitExecutor;
import model.instruction.system.RegisterChoiceExecutor;

import java.sql.SQLException;

public class OSS {

    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("Welcome to the Online Shopping System!");
        System.out.println();
        System.out.println("    [L] Login");
        System.out.println("    [R] Register");
        System.out.println("    [Q] Quit");
        System.out.println();
        System.out.print("Please enter your choice: ");
    }

    public void activate() throws SQLException {
        information();
        InputScanner scanner = new InputScanner();
        choose(scanner.getInput());
    }

    public void choose(String input) throws SQLException {
        switch (input) {
            case "L":
                LoginChoiceExecutor loginChoiceExecutor = new LoginChoiceExecutor();
                loginChoiceExecutor.executeInstruction();;
                break;
            case "R":
                RegisterChoiceExecutor registerChoiceExecutor = new RegisterChoiceExecutor();
                registerChoiceExecutor.executeInstruction();
                break;
            case "Q":
                QuitExecutor quitExecutor = new QuitExecutor();
                quitExecutor.executeInstruction();
                break;
            default:
                System.out.print("Invalid input! Please input again: ");
                InputScanner scanner1 = new InputScanner();
                choose(scanner1.getInput());
        }
    }

    public static boolean noQuotationMark(String s) {
        for (char c : s.toCharArray()) {
            if (c == '\'') {
                return false;
            }
        }
        return true;
    }

}
