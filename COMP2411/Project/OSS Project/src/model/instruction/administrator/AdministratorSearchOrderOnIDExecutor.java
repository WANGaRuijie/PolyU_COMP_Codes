package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.isRepeatedProductID;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorSearchOrderOnIDExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String orderID;

    public AdministratorSearchOrderOnIDExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now searching order according to Order ID");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the order ID of the order to search: ");
        this.orderID = getOrderID();
        System.out.println();
        visitDatabase();
        System.out.println();
        System.out.println("The information of the order is printed");
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
        String sql = "SELECT * FROM ORDER_ WHERE ORDER_ID = '" + this.orderID + "'";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

    public String getOrderID() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String orderID = productIDScanner.getInput().trim();
        if (!isValidOrderID(orderID)) {
            System.out.print("The ID does not exist! Please input again: ");
            return getOrderID();
        }
        return orderID;
    }

    public static boolean isValidOrderID(String orderID) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("ORDER_", "ORDER_ID", orderID)&&orderID.length()<=20;
    }
}
