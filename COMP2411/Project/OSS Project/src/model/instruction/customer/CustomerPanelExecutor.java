package model.instruction.customer;

import model.InputScanner;
import model.InstructionExecutor;
import model.OSS;
import model.instruction.system.QuitExecutor;

import java.sql.SQLException;

public class CustomerPanelExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public CustomerPanelExecutor(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    @Override
    public void information(){
        System.out.println("*********************************************************************************");
        System.out.println("Welcome, dear customer!");
        System.out.println();
        System.out.println("Below shows you the operations you can do");
        System.out.println();
        System.out.println("    [A1] Add a product to your shopping cart");
        System.out.println("    [A2] Adjust the quantity of the product in the shopping cart");
        System.out.println("    [D] Delete a product in your shopping cart");
        System.out.println("    [P] Preset your shipping addresses");
        System.out.println();
        System.out.println("    [F] Filter products");
        System.out.println("    [G] Get all products");
        System.out.println("    [S1] Search a product with a product ID");
        System.out.println("    [S2] Search products with a product name");
        System.out.println();
        System.out.println("    [M1] Modify your phone number");
        System.out.println("    [M2] Modify your user name");
        System.out.println("    [M3] Modify your password");
        System.out.println("    [M4] Modify your personal information");
        System.out.println("    [M5] Modify your shipping addresses");
        System.out.println();
        System.out.println("    [C] Checkout for your shopping cart");
        System.out.println("    [I] Inspect your shopping cart");
        System.out.println("    [R] Review your previous order");
        System.out.println("    [T] Top up your account");
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

            case "A1":
                CustomerAddProductExecutor customerAddProductExecutor = new CustomerAddProductExecutor(this.phoneNumber);
                customerAddProductExecutor.executeInstruction();
                break;

            case "A2":
                CustomerAdjustQuantityExecutor customerAdjustQuantityExecutor = new CustomerAdjustQuantityExecutor(this.phoneNumber);
                customerAdjustQuantityExecutor.executeInstruction();
                break;

            case "C":
                CustomerCheckoutExecutor customerCheckoutExecutor = new CustomerCheckoutExecutor(this.phoneNumber);
                customerCheckoutExecutor.executeInstruction();
                break;

            case "F":
                CustomerFilterExecutor customerFilterExecutor = new CustomerFilterExecutor(this.phoneNumber);
                customerFilterExecutor.executeInstruction();
                break;

            case "G":
                CustomerGetAllProductsExecutor customerGetAllProductsExecutor = new CustomerGetAllProductsExecutor(this.phoneNumber);
                customerGetAllProductsExecutor.executeInstruction();
                break;

            case "S1":
                CustomerSearchProductOnIDExecutor customerSearchProductOnIDExecutor = new CustomerSearchProductOnIDExecutor(this.phoneNumber);
                customerSearchProductOnIDExecutor.executeInstruction();
                break;

            case "S2":
                CustomerSearchProductOnNameExecutor customerSearchProductOnNameExecutor = new CustomerSearchProductOnNameExecutor(this.phoneNumber);
                customerSearchProductOnNameExecutor.executeInstruction();
                break;

            case "D":
                CustomerDeleteProductExecutor customerDeleteProductExecutor = new CustomerDeleteProductExecutor(this.phoneNumber);
                customerDeleteProductExecutor.executeInstruction();
                break;

            case "P":
                CustomerPresetShippingAddressExecutor customerPresetShippingAddressExecutor = new CustomerPresetShippingAddressExecutor(this.phoneNumber);
                customerPresetShippingAddressExecutor.executeInstruction();
                break;

            case "M1":
                CustomerModifyPhoneNumberExecutor customerModifyPhoneNumberExecutor = new CustomerModifyPhoneNumberExecutor(this.phoneNumber);
                customerModifyPhoneNumberExecutor.executeInstruction();
                break;

            case "M2":
                CustomerModifyUserNameExecutor customerModifyUserNameExecutor = new CustomerModifyUserNameExecutor(this.phoneNumber);
                customerModifyUserNameExecutor.executeInstruction();
                break;

            case "M3":
                CustomerModifyPasswordExecutor customerModifyPasswordExecutor = new CustomerModifyPasswordExecutor(this.phoneNumber);
                customerModifyPasswordExecutor.executeInstruction();
                break;

            case "M4":
                CustomerModifyPersonalInformationExecutor customerModifyPersonalInformationExecutor = new CustomerModifyPersonalInformationExecutor(this.phoneNumber);
                customerModifyPersonalInformationExecutor.executeInstruction();
                break;

            case "M5":
                CustomerModifyShippingAddressExecutor customerModifyShippingAddressExecutor = new CustomerModifyShippingAddressExecutor(this.phoneNumber);
                customerModifyShippingAddressExecutor.executeInstruction();
                break;

            case "I":
                CustomerPrintShoppingCartExecutor customerPrintShoppingCartExecutor = new CustomerPrintShoppingCartExecutor(this.phoneNumber);
                customerPrintShoppingCartExecutor.executeInstruction();
                break;

            case "T":
                CustomerTopUpExecutor customerTopUpExecutor = new CustomerTopUpExecutor(this.phoneNumber);
                customerTopUpExecutor.executeInstruction();
                break;

            case "R":
                CustomerReviewExecutor customerReviewExecutor = new CustomerReviewExecutor(this.phoneNumber);
                customerReviewExecutor.executeInstruction();
                break;

            case "L":
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
