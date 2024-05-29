package model.instruction.system;

import model.InputScanner;
import model.InstructionExecutor;
import model.OSS;

import java.sql.SQLException;

public class RegisterChoiceExecutor implements InstructionExecutor {

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("What kind of account you would like to register?");
        System.out.println();
        System.out.println("    [A] Administrator");
        System.out.println("    [C] Customer");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println(" ");
        System.out.print("Please enter your choice: ");
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        InputScanner scanner = new InputScanner();
        choose(scanner.getInput());
    }

    @Override
    public void choose(String input) throws SQLException {
        switch (input) {
            case "A":
                RegisterAdministratorExecutor registerAdministratorExecutor = new RegisterAdministratorExecutor();
                registerAdministratorExecutor.executeInstruction();
                break;
            case "C":
                RegisterCustomerExecutor registerCustomerExecutor = new RegisterCustomerExecutor();
                registerCustomerExecutor.executeInstruction();
                break;
            case "B":
                OSS newOSS = new OSS();
                newOSS.activate();
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

    @Override
    public void visitDatabase() throws SQLException {}

}
