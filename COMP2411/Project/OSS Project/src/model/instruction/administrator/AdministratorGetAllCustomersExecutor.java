package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdministratorGetAllCustomersExecutor implements InstructionExecutor {

    private String phoneNumber;

    public AdministratorGetAllCustomersExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now viewing all of the customers registered in the OSS system");
        System.out.println();
        printAllCustomerInformation();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.println();
        System.out.print("All of the customer information is printed");
        System.out.println();
        System.out.print("Enter any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
        administratorPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {}

    public void printAllCustomerInformation() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM CUSTOMER";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

}
