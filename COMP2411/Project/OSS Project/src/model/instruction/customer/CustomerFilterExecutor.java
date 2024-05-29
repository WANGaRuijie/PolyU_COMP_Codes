package model.instruction.customer;

import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.SQLException;

public class CustomerFilterExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public CustomerFilterExecutor(String phoneNumber) {
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
                CustomerFilterNameExecutor customerFilterNameExecutor = new CustomerFilterNameExecutor(this.phoneNumber);
                customerFilterNameExecutor.executeInstruction();
                break;

            case "F2":
                CustomerFilterDescriptionExecutor customerFilterDescriptionExecutor = new CustomerFilterDescriptionExecutor(this.phoneNumber);
                customerFilterDescriptionExecutor.executeInstruction();
                break;

            case "F3":
                CustomerFilterSpecificationExecutor customerFilterSpecificationExecutor = new CustomerFilterSpecificationExecutor(this.phoneNumber);
                customerFilterSpecificationExecutor.executeInstruction();
                break;

            case "F4":
                CustomerFilterPriceExecutor customerFilterPriceExecutor = new CustomerFilterPriceExecutor(this.phoneNumber);
                customerFilterPriceExecutor.executeInstruction();
                break;

            case "F5":
                CustomerFilterCategoryExecutor customerFilterCategoryExecutor = new CustomerFilterCategoryExecutor(this.phoneNumber);
                customerFilterCategoryExecutor.executeInstruction();
                break;

            case "B":
                CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
                customerPanelExecutor.executeInstruction();
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
