package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.isRepeatedProductID;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorSearchShoppingCartOnProductNameExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String productName;

    public AdministratorSearchShoppingCartOnProductNameExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now searching shopping cart according to product name");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the product name of product included in the shopping cart to search: ");
        this.productName = getProductName();
        System.out.println();
        visitDatabase();
        System.out.println();
        System.out.println("The information of the shopping cart is printed");
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
        String sql = "SELECT * FROM SHOPPING_CART WHERE PRODUCT_NAME = '" + this.productName + "'";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
    }

    public String getProductName() throws SQLException {
        InputScanner productNameScanner = new InputScanner();
        if (productNameScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productNameScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String productName = productNameScanner.getInput().trim();
        if (!isExistedProductName(productName)) {
            System.out.print("The product name does not exist! Please input again: ");
            return getProductName();
        }
        return productName;
    }

    public static boolean isExistedProductName(String productName) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("PRODUCT", "PRODUCT_NAME", productName);
    }
}
