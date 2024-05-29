package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerAddProductExecutor implements InstructionExecutor {

    private final String phoneNumber;
    private String productID;
    private String productName;
    private double singlePrice;
    private double totalPrice;
    private int quantity;


    public CustomerAddProductExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now adding a product to your shopping cart");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        confirm();
        System.out.print("Please enter the ID of the product you want to add to your shopping cart: ");
        this.productID = getProductID();
        if(isRepeatedProductID(this.productID) && isExistedPhoneNumber(this.phoneNumber)){
            System.out.println("You are adding a product which is already in your shopping cart.");
            addRepeatedProduct();
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        System.out.print("Please enter the quantity of the product you want to add to your shopping cart: ");
        this.quantity = getQuantity();
        this.productName = getProductName();
        this.singlePrice = getSinglePrice();
        this.totalPrice = quantity * singlePrice;
        visitDatabase();
        System.out.println();
        System.out.println("The item has been successfully added to your shopping cart");
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "INSERT INTO SHOPPING_CART (CUSTOMER_PHONE_NUMBER, PRODUCT_ID, PRODUCT_NAME, QUANTITY, SINGLE_PRICE, TOTAL_PRICE) VALUES ('" + this.phoneNumber + "', '" + this.productID + "', '" + this.productName + "', '" + this.quantity + "', '" + this.singlePrice + "', '" + this.totalPrice + "')";
        try {
            db.insert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProductID() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
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


    public int getQuantity () throws SQLException {
        InputScanner quantityScanner = new InputScanner();
        if (quantityScanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (quantityScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if (!isValidQuantityInput(quantityScanner.getInput().trim())) {
            System.out.print("Invalid quantity! Please input again: ");
            return getQuantity();
        }

        int quantity = Integer.parseInt(quantityScanner.getInput().trim());
        if (!isValidQuantity(quantity, this.productID)) {
            System.out.print("The quantity input should not be larger than the current available quantity of the product! Please input again: ");
            return getQuantity();
        }
        return quantity;
    }


    public boolean isExistedProductID(String productID) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("PRODUCT", "PRODUCT_ID", productID);
    }

    public boolean isRepeatedProductID(String productID) throws SQLException{
        Database db = Database.getDataBase();
        return db.contains("SHOPPING_CART", "PRODUCT_ID", productID);
    }

    public ResultSet getProductInformation(String productID) throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT PRODUCT_NAME, PRICE FROM PRODUCT WHERE PRODUCT_ID = '" + productID + "'";
        return db.query(sql);
    }

    public String getProductName() throws SQLException{
        ResultSet queryResult = getProductInformation(this.productID);
        String productName = null;
        while (queryResult.next()) {
            productName = queryResult.getString("PRODUCT_NAME");
        }
        return productName;
    }

    public double getSinglePrice() throws SQLException{
        ResultSet queryResult = getProductInformation(this.productID);
        if (queryResult.next()) {
            return queryResult.getDouble("PRICE");
        }
        return 0;
    }

    public boolean isExistedPhoneNumber(String phoneNumber) throws SQLException{
        Database db = Database.getDataBase();
        return db.contains("SHOPPING_CART", "CUSTOMER_PHONE_NUMBER", phoneNumber);
    }

    public static boolean isValidQuantityInput(String quantityInput) {
        try {
            int quantity = Integer.parseInt(quantityInput);
            if (quantity <= 0) {
                return false;
            }
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }

    public static boolean isValidQuantity(int quantity, String productID) throws SQLException {
        String sql = "SELECT AVAILABLE_QUANTITY FROM PRODUCT WHERE PRODUCT_ID = '" + productID + "'";
        double realQuantity = 0;
        Database db = Database.getDataBase();
        ResultSet rs = db.query(sql);
        if (rs.next()) {
            realQuantity = rs.getDouble("AVAILABLE_QUANTITY");
        }
        return !(realQuantity < quantity);
    }

    public void addRepeatedProduct() throws SQLException{
        System.out.println("Please input a new quantity for the repeated product in your shopping cart.");
        int newQuantity =  getQuantity();
        double singlePrice = getSinglePrice();
        double totalPrice = newQuantity * singlePrice;
        Database db = Database.getDataBase();
        String numberUpdate = "UPDATE SHOPPING_CART SET QUANTITY ='"+newQuantity+"' WHERE CUSTOMER_PHONE_NUMBER ='"+this.phoneNumber+"' AND PRODUCT_ID = '"+this.productID+"'";
        String priceUpdate = "UPDATE SHOPPING_CART SET TOTAL_PRICE = '"+totalPrice+"' WHERE CUSTOMER_PHONE_NUMBER ='"+this.phoneNumber+"' AND PRODUCT_ID = '"+this.productID+"'";
        try{
            db.update(numberUpdate);
            db.update(priceUpdate);
        } catch(Exception e) {
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

}
