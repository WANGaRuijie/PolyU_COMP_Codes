package model.instruction.administrator;

import model.InputScanner;
import model.InstructionExecutor;
import model.OSS;
import model.instruction.system.QuitExecutor;

import java.sql.SQLException;

public class AdministratorPanelExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public AdministratorPanelExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("Hello, our distinguished administrator!");
        System.out.println();
        System.out.println("    [A] Add a new product");
        System.out.println("    [D] Delete a product");
        System.out.println("    [E] Edit a product");
        System.out.println("    [F] Filter the products");
        System.out.println("    [G1] Get all products");
        System.out.println("    [S1] Search a product with a product ID");
        System.out.println("    [S2] Search products with a product name");
        System.out.println();
        System.out.println("    [G2] Get all customers");
        System.out.println("    [S3] Search a customer with a phone number");
        System.out.println("    [S4] Search customers with a user name");
        System.out.println();
        System.out.println("    [G3] Get all orders");
        System.out.println("    [S5] Search an order with a order ID");
        System.out.println();
        System.out.println("    [G4] Get all shopping cart information");
        System.out.println("    [S6] Search shopping cart information with a product ID");
        System.out.println("    [S7] Search shopping cart information with a product name");
        System.out.println("    [S8] Search shopping cart information with a user phone number");
        System.out.println();
        System.out.println("    [M1] Modify your phone number");
        System.out.println("    [M2] Modify your user name");
        System.out.println("    [M3] Modify your password");
        System.out.println("    [M4] Modify your personal information");
        System.out.println();
        System.out.println("    [G5] Generate analysis report");
        System.out.println();
        System.out.println("    [L] Log out");
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

            case "A":
                AdministratorAddProductExecutor administratorAddProductExecutor = new AdministratorAddProductExecutor(this.phoneNumber);
                administratorAddProductExecutor.executeInstruction();
                break;

            case "D":
                AdministratorDeleteProductExecutor administratorDeleteProductExecutor = new AdministratorDeleteProductExecutor(this.phoneNumber);
                administratorDeleteProductExecutor.executeInstruction();
                break;

            case "E":
                AdministratorEditProductExecutor administratorEditProductExecutor = new AdministratorEditProductExecutor(this.phoneNumber);
                administratorEditProductExecutor.executeInstruction();
                break;

            case "F":
                AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
                administratorFilterExecutor.executeInstruction();
                break;

            case "G1":
                AdministratorGetAllProductsExecutor administratorGetAllProductsExecutor = new AdministratorGetAllProductsExecutor(this.phoneNumber);
                administratorGetAllProductsExecutor.executeInstruction();
                break;


            case "S1":
                AdministratorSearchProductOnIDExecutor administratorSearchProductOnIDExecutor = new AdministratorSearchProductOnIDExecutor(this.phoneNumber);
                administratorSearchProductOnIDExecutor.executeInstruction();
                break;

            case "S2":
                AdministratorSearchProductOnNameExecutor administratorSearchProductOnNameExecutor = new AdministratorSearchProductOnNameExecutor(this.phoneNumber);
                administratorSearchProductOnNameExecutor.executeInstruction();
                break;

            case "G2":
                AdministratorGetAllCustomersExecutor administratorGetAllCustomersExecutor = new AdministratorGetAllCustomersExecutor(this.phoneNumber);
                administratorGetAllCustomersExecutor.executeInstruction();
                break;

            case "S3":
                AdministratorSearchCustomerOnPhoneNumberExecutor administratorSearchCustomerOnPhoneNumberExecutor = new AdministratorSearchCustomerOnPhoneNumberExecutor(this.phoneNumber);
                administratorSearchCustomerOnPhoneNumberExecutor.executeInstruction();
                break;

            case "S4":
                AdministratorSearchCustomerOnNameExecutor administratorSearchCustomerOnNameExecutor = new AdministratorSearchCustomerOnNameExecutor(this.phoneNumber);
                administratorSearchCustomerOnNameExecutor.executeInstruction();
                break;

            case "G3":
                AdministratorGetAllOrderExecutor administratorGetAllOrderExecutor = new AdministratorGetAllOrderExecutor(this.phoneNumber);
                administratorGetAllOrderExecutor.executeInstruction();
                break;

            case "S5":
                AdministratorSearchOrderOnIDExecutor administratorSearchOrderOnIDExecutor = new AdministratorSearchOrderOnIDExecutor(this.phoneNumber);
                administratorSearchOrderOnIDExecutor.executeInstruction();
                break;

            case "G4":
                AdministratorGetAllShoppingCartExecutor administratorGetAllShoppingCartExecutor = new AdministratorGetAllShoppingCartExecutor(this.phoneNumber);
                administratorGetAllShoppingCartExecutor.executeInstruction();
                break;

            case "S6":
                AdministratorSearchShoppingCartOnProductIDExecutor administratorSearchShoppingCartOnProductIDExecutor = new AdministratorSearchShoppingCartOnProductIDExecutor(this.phoneNumber);
                administratorSearchShoppingCartOnProductIDExecutor.executeInstruction();
                break;

            case "S7":
                AdministratorSearchShoppingCartOnProductNameExecutor administratorSearchShoppingCartOnProductNameExecutor = new AdministratorSearchShoppingCartOnProductNameExecutor(this.phoneNumber);
                administratorSearchShoppingCartOnProductNameExecutor.executeInstruction();
                break;

            case "S8":
                AdministratorSearchShoppingCartOnUserPhoneNumberExecutor administratorSearchShoppingCartOnUserPhoneNumberExecutor = new AdministratorSearchShoppingCartOnUserPhoneNumberExecutor(this.phoneNumber);
                administratorSearchShoppingCartOnUserPhoneNumberExecutor.executeInstruction();
                break;

            case "M1":
                AdministratorModifyPhoneNumberExecutor administratorModifyPhoneNumberExecutor = new AdministratorModifyPhoneNumberExecutor(this.phoneNumber);
                administratorModifyPhoneNumberExecutor.executeInstruction();
                break;

            case "M2":
                AdministratorModifyUserNameExecutor administratorModifyUserNameExecutor = new AdministratorModifyUserNameExecutor(this.phoneNumber);
                administratorModifyUserNameExecutor.executeInstruction();
                break;

            case "M3":
                AdministratorModifyPasswordExecutor administratorModifyPasswordExecutor = new AdministratorModifyPasswordExecutor(this.phoneNumber);
                administratorModifyPasswordExecutor.executeInstruction();
                break;

            case "M4":
                AdministratorModifyPersonalInformationExecutor administratorModifyPersonalInformationExecutor = new AdministratorModifyPersonalInformationExecutor(this.phoneNumber);
                administratorModifyPersonalInformationExecutor.executeInstruction();
                break;

            case "G5":
                AdministratorGenerateReportExecutor administratorGenerateReportExecutor = new AdministratorGenerateReportExecutor(this.phoneNumber);
                administratorGenerateReportExecutor.executeInstruction();
                break;

            case "L":
                System.out.println();
                System.out.println("Account logged out successfully");
                OSS newOSS = new OSS();
                newOSS.activate();
                break;

            case "Q":
                QuitExecutor quitExecutor = new QuitExecutor();
                quitExecutor.executeInstruction();

            default:
                System.out.print("Invalid input! Please input again: ");
                InputScanner scanner1 = new InputScanner();
                choose(scanner1.getInput());

        }
    }

    @Override
    public void visitDatabase() throws SQLException {}

}
