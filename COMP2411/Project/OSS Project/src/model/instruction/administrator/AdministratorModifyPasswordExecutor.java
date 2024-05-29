package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;
import model.instruction.system.RegisterCustomerExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorModifyPasswordExecutor implements InstructionExecutor {
    private String phoneNumber;
    private String password;

    public AdministratorModifyPasswordExecutor(String phoneNumber) {
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
        System.out.println("Are you sure you want to edit your password?");
        showResultSetInformation(getPersonalPassword(this.phoneNumber));
        System.out.println();
        System.out.print("Enter Y to confirm the edition, or any key to cancel: ");
        InputScanner confirmationScanner = new InputScanner();
        if (confirmationScanner.getInput().trim().equals("Y")) {
            System.out.println();
            System.out.print("Please enter the new value for the password: ");
            this.password = getPassword();
            visitDatabase();
            System.out.println();
            System.out.println("The password is successfully modified");
            System.out.print("Enter any key to continue: ");
            InputScanner confirmationScanner1 = new InputScanner();
        } else {
            System.out.println("Edition canceled");
        }
        AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
        administratorPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        String sql;
        sql = "UPDATE ADMIN SET " + "PASSWORD" + " = '" + this.password + "' WHERE PHONE_NUMBER = '" + this.phoneNumber + "'";
        Database db = Database.getDataBase();
        try {
            db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getPersonalPassword(String targetPhoneNumber) throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM ADMIN WHERE PHONE_NUMBER = '" + targetPhoneNumber + "'";
        return db.query(sql);
    }
    public String getPassword() throws SQLException {
        InputScanner passwordScanner = new InputScanner();
        if (passwordScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (passwordScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String password = passwordScanner.getInput().trim();

        if (!RegisterCustomerExecutor.isValidPassword(password)) {
            System.out.print("Invalid input! Please input again: ");
            return getPassword();
        }
        return password;
    }
}