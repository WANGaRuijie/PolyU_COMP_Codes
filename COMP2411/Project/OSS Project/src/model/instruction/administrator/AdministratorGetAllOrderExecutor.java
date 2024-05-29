package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorGetAllOrderExecutor implements InstructionExecutor {

    private String phoneNumber;

    public AdministratorGetAllOrderExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now viewing all of the orders added in the OSS system");
        System.out.println();
        printAllOrderInformation();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.println();
        System.out.print("All of the order information is printed");
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
    public void printAllOrderInformation() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM ORDER_";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

}
