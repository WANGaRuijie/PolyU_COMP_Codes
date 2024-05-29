package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.isRepeatedProductID;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorSearchShoppingCartOnProductIDExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String productID;

    public AdministratorSearchShoppingCartOnProductIDExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now searching shopping cart according to product ID");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the product ID of product included in the shopping cart to search: ");
        this.productID = getProductID();
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
        String sql = "SELECT * FROM SHOPPING_CART WHERE PRODUCT_ID = '" + this.productID + "'";
        ResultSet rs = db.query(sql);
        showResultSetInformation(rs);
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

        if (!isRepeatedProductID(productID)) {
            System.out.print("The product ID does not exist! Please input again: ");
            return getProductID();
        }
        return productID;
    }
}
