package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class CustomerPrintShoppingCartExecutor implements InstructionExecutor {
    private final String phoneNumber;


    public CustomerPrintShoppingCartExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        printShoppingCart();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.println();
        System.out.print("All of the shopping cart information is printed");
        System.out.println();
        System.out.print("Enter any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {
    }

    @Override
    public void visitDatabase() throws SQLException {

    }
    public void printShoppingCart() throws SQLException{
        System.out.println("Here are the current details of your shopping cart");
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
        ResultSet shoppingCart = db.query(sql);
        System.out.println();
        while(shoppingCart.next()){
            String productID = shoppingCart.getString("PRODUCT_ID");
            String productName = shoppingCart.getString("PRODUCT_NAME");
            int quantity = shoppingCart.getInt("QUANTITY");
            double singlePrice = shoppingCart.getDouble("SINGLE_PRICE");
            double totalPrice = shoppingCart.getDouble("TOTAL_PRICE");
            System.out.println("PRODUCT_ID: "+productID+", PRODUCT_NAME: "+productName+", QUANTITY: "+quantity+", SINGLE_PRICE: "+singlePrice+", TOTAL_PRICE: "+totalPrice);
        }
    }
}