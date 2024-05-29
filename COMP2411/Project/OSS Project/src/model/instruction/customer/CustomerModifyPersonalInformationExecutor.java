package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;
import static model.instruction.administrator.AdministratorModifyPersonalInformationExecutor.isValidPersonalInformation;


public class CustomerModifyPersonalInformationExecutor implements InstructionExecutor {
    private final String phoneNumber;
    private String personalInformation;

    public CustomerModifyPersonalInformationExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now editing your personal information");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.println("Are you sure you want to edit the personal information of your account?");
        showResultSetInformation(getPersonalInformation(this.phoneNumber));
        System.out.println();
        System.out.print("Enter Y to confirm the edition, or any key to cancel: ");
        InputScanner confirmationScanner = new InputScanner();
        if (confirmationScanner.getInput().trim().equals("Y")) {
            System.out.println();
            System.out.print("Please enter the new value for the personal information: ");
            this.personalInformation = getPersonalInformation();
            visitDatabase();
            System.out.println();
            System.out.println("The personal information is successfully modified");
            System.out.println();
            System.out.print("Enter any key to continue: ");
            InputScanner confirmationScanner1 = new InputScanner();
        } else {
            System.out.println("Edition canceled");
        }
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        String sql;
        sql = "UPDATE CUSTOMER SET " + "PERSONAL_INFORMATION" + " = '" + this.personalInformation+ "' WHERE PHONE_NUMBER = '" + this.phoneNumber + "'";
        Database db = Database.getDataBase();
        try {
            db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String getPersonalInformation() throws SQLException {
        InputScanner personalInformationScanner = new InputScanner();
        if (personalInformationScanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (personalInformationScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String personalInformation = personalInformationScanner.getInput().trim();

        if (!isValidPersonalInformation(personalInformation)) {
            System.out.print("Invalid product ID! Please input again: ");
            return getPersonalInformation();
        }
        return personalInformation;
    }

    public static ResultSet getPersonalInformation(String targetPhoneNumber) throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM CUSTOMER WHERE PHONE_NUMBER = '" + targetPhoneNumber + "'";
        return db.query(sql);
    }
}