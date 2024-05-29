package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorSearchCustomerOnNameExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String customerName;

    public AdministratorSearchCustomerOnNameExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now searching the customer according to customer name");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the name of the customer to search: ");
        this.customerName = getCustomerName();
        System.out.println();
        visitDatabase();
        System.out.println();
        System.out.println("The information of the customer is printed");
        System.out.println();
        System.out.print("Enter any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
        administratorPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM CUSTOMER WHERE USER_NAME = '" + this.customerName + "'";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

    public String getCustomerName() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String customerName = productIDScanner.getInput().trim();

        if (!isValidcustomerName(customerName)) {
            System.out.print("The name does not exist! Please input again: ");
            return getCustomerName();
        }
        return customerName;
    }

    public static boolean isValidcustomerName(String customerName) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("CUSTOMER", "USER_NAME", customerName)&&customerName.length()<=20;
    }
}
