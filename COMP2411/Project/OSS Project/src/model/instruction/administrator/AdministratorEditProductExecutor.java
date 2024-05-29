package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.*;

public class AdministratorEditProductExecutor implements InstructionExecutor {

    private final String phoneNumber;

    private String productID;
    private String propertyName;
    private String newValue;

    public AdministratorEditProductExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now editing a product");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please enter the product ID of the product to edit: ");
        this.productID = getProductID();
        System.out.println();
        System.out.println("Are you sure you to edit the product?");
        AdministratorDeleteProductExecutor.showResultSetInformation(AdministratorDeleteProductExecutor.getProductInformation(this.productID));
        System.out.println();
        System.out.print("Enter Y to start the edition, or any key to cancel: ");
        InputScanner confirmationScanner = new InputScanner();
        if (confirmationScanner.getInput().trim().equals("Y")) {
            System.out.println();
            System.out.print("Please enter the name of the property to edit (column name): ");
            this.propertyName = getPropertyName();
            System.out.print("Please enter the new value for the property: ");
            this.newValue = getNewValue();
            visitDatabase();
        } else {
            System.out.println("Edition canceled");
        }
        AdministratorEditProductExecutor administratorEditProductExecutor = new AdministratorEditProductExecutor(this.phoneNumber);
        administratorEditProductExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        String sql;
        if (this.propertyName.equals("PRICE") || this.propertyName.equals("AVAILABLE_QUANTITY")) {
            sql = "UPDATE PRODUCT SET " + this.propertyName + " = " + this.newValue + " WHERE PRODUCT_ID = '" + this.productID + "'";
        } else {
            sql = "UPDATE PRODUCT SET " + this.propertyName + " = '" + this.newValue + "' WHERE PRODUCT_ID = '" + this.productID + "'";
        }
        Database db = Database.getDataBase();
        try {
            db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        if (!AdministratorDeleteProductExecutor.isExistedProductID(productID)) {
            System.out.print("Invalid product ID! Please input again: ");
            return getProductID();
        }
        return productID;
    }

    public String getPropertyName() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String propertyName = productIDScanner.getInput().trim();
        if (!isColumnName(propertyName)) {
            System.out.print("Invalid property name! Please input again: ");
            return  getPropertyName();
        }
        return propertyName;
    }

    public boolean isColumnName(String propertyName) {
        try {
            ResultSet rs = AdministratorDeleteProductExecutor.getProductInformation(this.productID);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsmd.getColumnName(i);
                if (columnName.equalsIgnoreCase(propertyName)) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getNewValue() throws SQLException {
        InputScanner newValueScanner = new InputScanner();
        if (newValueScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (newValueScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String newValue = newValueScanner.getInput().trim();
        if (!isValidNewValue(newValue)) {
            System.out.print("Invalid newValue! Please input again: ");
            return getNewValue();
        }
        return newValue;
    }

    public boolean isValidNewValue(String newValue) throws SQLException {

        if (this.propertyName.equals("PRODUCT_ID")) {
            return isValidProductID(newValue);
        }

        if (this.propertyName.equals("PRODUCT_NAME")) {
            return isValidProductName(newValue);
        }

        if (this.propertyName.equals("DESCRIPTION")) {
            return isValidDescription(newValue);
        }

        if (this.propertyName.equals("SPECIFICATION")) {
            return isValidSpecification(newValue);
        }

        if (this.propertyName.equals("PRICE")) {
            return isValidPriceInput(newValue);
        }

        if (this.propertyName.equals("AVAILABLE_QUANTITY")) {
            return isValidQuantityInput(newValue);
        }

        if (this.propertyName.equals("CATEGORY")) {
            return  isValidCategory(newValue);
        }

        return false;
    }

}