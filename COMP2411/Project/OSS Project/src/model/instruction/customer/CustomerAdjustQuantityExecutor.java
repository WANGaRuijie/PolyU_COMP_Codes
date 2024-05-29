package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static database.Database.getDataBase;

public class CustomerAdjustQuantityExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public CustomerAdjustQuantityExecutor(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information(){
        System.out.println("*********************************************************************************");
        System.out.println("You are now adjusting the quantity of the products in your shopping cart.");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
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


    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void executeInstruction() throws SQLException{
        information();
        confirm();
        visitDatabase();
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void visitDatabase() throws SQLException{
        printShoppingCart();
        modifyQuantity();
    }

    public void printShoppingCart() throws SQLException{
        System.out.println();
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


    public void modifyQuantity() throws SQLException{
        System.out.println();
        System.out.print("Please enter the productID of the product that you want to change its quantity: ");
        InputScanner productID = new InputScanner();
        if (productID.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (productID.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if(!isExistedProductID(productID.getInput())){
            System.out.println();
            System.out.println("Invalid input. Please enter a correct productID");
            modifyQuantity();
        }
        else{
            System.out.println();
            System.out.println("The product is successfully found");
            printAvailableQuantity(productID.getInput());
            System.out.println();
            System.out.print("Please enter a new quantity you want for that product: ");
            InputScanner newQuantity = new InputScanner();
            if(Integer.parseInt(newQuantity.getInput())>getQuantity(productID.getInput())[0]){
                System.out.println();
                System.out.println("Your input has exceeded the inventory amount. Try again!");
                modifyQuantity();
            }
            else{
                Database db = getDataBase();
                double totalPrice = Integer.parseInt(newQuantity.getInput()) * (getSinglePrice(productID.getInput()));
                String updateQuantity = "UPDATE SHOPPING_CART SET QUANTITY = '"+newQuantity.getInput()+"' WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"' AND PRODUCT_ID = '"+productID.getInput()+"'";
                String updateTotalPrice = "UPDATE SHOPPING_CART SET TOTAL_PRICE = '"+totalPrice+"' WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"' AND PRODUCT_ID = '"+productID.getInput()+"'";
                try{
                    db.update(updateQuantity);
                    db.update(updateTotalPrice);
                }catch(Exception e){
                    e.printStackTrace();
                }
                System.out.println();
                System.out.println("The quantity has been successfully modified to "+ newQuantity .getInput().trim());
            }
        }
    }
    public boolean isExistedProductID(String productID) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("PRODUCT", "PRODUCT_ID", productID);
    }

    public void printAvailableQuantity(String productID) throws SQLException{
        System.out.println();
        System.out.println("Here is the amount of the inventory of that product");
        Database db = getDataBase();
        String sql = "SELECT AVAILABLE_QUANTITY FROM PRODUCT WHERE PRODUCT_ID = '"+ productID +"'";
        ResultSet queryResult = db.query(sql);
        while(queryResult.next()){
            int inventory = queryResult.getInt("AVAILABLE_QUANTITY");
            System.out.println("INVENTORY: "+inventory);
        }
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

    public int[] getQuantity(String productID) throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT AVAILABLE_QUANTITY FROM PRODUCT WHERE PRODUCT_ID = '"+productID+"'";
        ResultSet resultSet = db.query(sql);
        Object[] quantity = sqlResultToArray(resultSet);
        int[] intArray = new int[quantity.length];
        for (int i = 0; i < quantity.length; i++) {
            intArray[i] = Integer.parseInt((String) quantity[i]);
        }
        return intArray;
    }

    public double getSinglePrice(String productID) throws SQLException{
        ResultSet queryResult = getProductInformation(productID);
        if (queryResult.next()) {
            return queryResult.getDouble("PRICE");
        }
        return 0;
    }

    public ResultSet getProductInformation(String productID) throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT PRODUCT_NAME, PRICE FROM PRODUCT WHERE PRODUCT_ID = '" + productID + "'";
        return db.query(sql);
    }

}