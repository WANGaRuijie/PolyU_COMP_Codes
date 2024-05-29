package model.instruction.administrator;

import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.SQLException;

public class AdministratorFilterExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public AdministratorFilterExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now filtering the products");
        System.out.println();
        System.out.println("    [F1] Filter with a key word in the product name");
        System.out.println("    [F2] Filter with a key word in the product description");
        System.out.println("    [F3] Filter with a key word in the product specification");
        System.out.println("    [F4] Filter with a criterion on the product price");
        System.out.println("    [F5] Filter with a key word in the product category");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
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

            case "F1":
                AdministratorFilterNameExecutor administratorFilterNameExecutor = new AdministratorFilterNameExecutor(this.phoneNumber);
                administratorFilterNameExecutor.executeInstruction();
                break;

            case "F2":
                AdministratorFilterDescriptionExecutor administratorFilterDescriptionExecutor = new AdministratorFilterDescriptionExecutor(this.phoneNumber);
                administratorFilterDescriptionExecutor.executeInstruction();
                break;

            case "F3":
                AdministratorFilterSpecificationExecutor administratorFilterSpecificationExecutor = new AdministratorFilterSpecificationExecutor(this.phoneNumber);
                administratorFilterSpecificationExecutor.executeInstruction();
                break;

            case "F4":
                AdministratorFilterPriceExecutor administratorFilterPriceExecutor = new AdministratorFilterPriceExecutor(this.phoneNumber);
                administratorFilterPriceExecutor.executeInstruction();
                break;

            case "F5":
                AdministratorFilterCategoryExecutor administratorFilterCategoryExecutor = new AdministratorFilterCategoryExecutor(this.phoneNumber);
                administratorFilterCategoryExecutor.executeInstruction();
                break;

            case "B":
                AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
                administratorPanelExecutor.executeInstruction();
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
