package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDeleteProductExecutor implements InstructionExecutor {
    private final String phoneNumber;
    private String productID;

    public CustomerDeleteProductExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now removing a product from your shopping cart");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        confirm();
        System.out.println("Please enter the ID of the product you want to remove from your shopping cart");
        this.productID = getInputProductID();
        visitDatabase();
        System.out.println();
        System.out.println("The item has been successfully removed from your shopping cart");
        CustomerDeleteProductExecutor customerDeleteProductExecutor = new CustomerDeleteProductExecutor(this.phoneNumber);
        customerDeleteProductExecutor.executeInstruction();
    }
    public String getInputProductID() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        else if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String productID = productIDScanner.getInput().trim();
        if  (!isExistedProductID(productID)) {
            System.out.println("The input product ID is not detected in your shopping cart. Try again.");
            System.out.println("If you don't want to proceed, press B for back or Q for quit.");
            return getInputProductID();
        }
        return productID;
    }
    public boolean isExistedProductID(String productID) throws SQLException {
        String[] expectedPID = getProductIDInShoppingCart();
        int i = 0;
        while(i<expectedPID.length){
            if(expectedPID[i].equals(productID)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "DELETE FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "' AND PRODUCT_ID = '"+ this.productID+"'";
        try {
            db.insert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void confirm() throws SQLException{
        System.out.print("If you don't want to quit or go back, press any key except B and Q: ");
        InputScanner scanner = new InputScanner();
        System.out.println();
        if (scanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (scanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
    }
    public String[] getProductIDInShoppingCart () throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT PRODUCT_ID FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"'";
        ResultSet resultSet = db.query(sql);
        String[] productID = (String[]) sqlResultToArray(resultSet);
        return productID;
    }
    public  Object[] sqlResultToArray(ResultSet resultSet) throws SQLException {
        List<String> resultList = new ArrayList<>();
        while (resultSet.next()) {
            String productId = resultSet.getString(1);
            resultList.add(productId);
        }
        String[] resultArray = resultList.toArray(new String[0]);
        return resultArray;
    }


}
