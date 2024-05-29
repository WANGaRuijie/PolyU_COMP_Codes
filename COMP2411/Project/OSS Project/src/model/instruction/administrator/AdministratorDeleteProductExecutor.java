package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class AdministratorDeleteProductExecutor implements InstructionExecutor {
    private String phoneNumber;

    private String productID;

    public AdministratorDeleteProductExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now deleting a product");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the product ID of the product to delete: ");
        this.productID = getProductID();
        System.out.println();
        System.out.println("Are you sure to delete the product?");
        showResultSetInformation(getProductInformation(this.productID));
        System.out.println();
        System.out.print("Enter Y to confirm the deletion, or any key to cancel: ");
        InputScanner confirmationScanner = new InputScanner();
        if (confirmationScanner.getInput().trim().equals("Y")) {
            visitDatabase();
            System.out.println("The product is successfully deleted");
        } else {
            System.out.println("Deletion canceled");
        }
        AdministratorDeleteProductExecutor administratorDeleteProductExecutor = new AdministratorDeleteProductExecutor(this.phoneNumber);
        administratorDeleteProductExecutor.executeInstruction();
    }

    public String getProductID() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String productID = productIDScanner.getInput().trim();

        if (!isExistedProductID(productID)) {
            System.out.print("Invalid product ID! Please input again: ");
            return getProductID();
        }
        return productID;
    }

    public static boolean isExistedProductID(String productID) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("PRODUCT", "PRODUCT_ID", productID);
    }

    public static ResultSet getProductInformation(String productID) throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = '" + productID + "'";
        return db.query(sql);
    }

    public static void showResultSetInformation(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();

        if (rs.next()) {
            do {
                System.out.println();
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    String columnValue = rs.getString(i);
                    System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                }
                System.out.println();
            } while (rs.next());
        } else {
            System.out.println();
            System.out.println("The result is empty");
        }
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID = '" + this.productID  + "'";
        try {
            db.delete(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
