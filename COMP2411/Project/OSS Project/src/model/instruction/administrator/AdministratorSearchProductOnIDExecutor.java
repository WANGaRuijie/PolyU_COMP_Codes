package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.isRepeatedProductID;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorSearchProductOnIDExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String productID;

    public AdministratorSearchProductOnIDExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now searching a product according to product ID");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the product ID of the product to search: ");
        this.productID = getProductID();
        System.out.println();
        visitDatabase();
        System.out.println();
        System.out.println("The information of the product is printed");
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
        String sss = "SELECT p.PRODUCT_ID, p.PRODUCT_NAME, p.DESCRIPTION, p.SPECIFICATION, p.PRICE, p.AVAILABLE_QUANTITY, p.CATEGORY, r.CONTENT " +
                "FROM PRODUCT p " +
                "LEFT JOIN REVIEW r ON p.PRODUCT_ID = r.PRODUCT_ID " +
                "WHERE p.PRODUCT_ID = '" + this.productID + "'";

        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = '" + this.productID + "'";
        ResultSet rs = db.query(sss);
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
            System.out.print("The product does not exist! Please input again: ");
            return getProductID();
        }
        return productID;
    }
}
