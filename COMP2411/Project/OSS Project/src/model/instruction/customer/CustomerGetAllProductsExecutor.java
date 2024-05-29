package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class CustomerGetAllProductsExecutor implements InstructionExecutor {

    private final String phoneNumber;

    private String productID;

    public CustomerGetAllProductsExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now viewing all of the products added in the OSS system");
        System.out.println();
        printAllProductInformation();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.println();
        System.out.print("All of the product information is printed");
        System.out.println();
        System.out.print("Enter any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {}
    public void printAllProductInformation() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM PRODUCT";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

}
