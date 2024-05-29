package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import static model.OSS.noQuotationMark;


public class CustomerCheckoutExecutor implements InstructionExecutor {
    private final String phoneNumber;
    private String orderID;
    private String shippingAddress;
    public CustomerCheckoutExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now checking out for your shopping cart");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }
    @Override
    public void executeInstruction() throws SQLException{
        information();
        confirm();
        visitDatabase();
        System.out.println("Do you want to proceed to make your payment?");
        System.out.println();
        System.out.println("    [Y] Yes");
        System.out.println("    [B] Back");
        System.out.println();
        System.out.print("Please enter your choice: ");
        proceedOrNot();
        if (checkQuantity()) {
            updateAvailableQuantity();
            pay();
            System.out.println("Please select one way of determining your shipping address.");
            System.out.println();
            System.out.println("    [1] Set an address immediately.");
            System.out.println("    [2] Select a preset address");
            System.out.println();
            checkChoice();
        } else {
            System.out.println("Please adjust the quantity of the product in your shopping cart.");
            System.out.println();
            System.out.print("Press any key to continue: ");
            InputScanner confirmationScanner = new InputScanner();
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }

    }

    @Override
    public void choose(String input) throws SQLException {}

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
    public void visitDatabase() throws SQLException {
        System.out.println();
        System.out.println("The total amount of money you need to pay is "+getTotalMoney());
        System.out.println();
    }

    public double getTotalMoney() throws SQLException{
        Database db = Database.getDataBase();
        double totalMoney = 0;
        String sql = "SELECT SUM(TOTAL_PRICE) AS TOTAL_SUM FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '"+ this.phoneNumber + "'";
        ResultSet queryResult = db.query(sql);
        while (queryResult.next()) {
            totalMoney = queryResult.getDouble("TOTAL_SUM");
        }
        return totalMoney;
    }

    public double getBalance() throws SQLException{
        Database db = Database.getDataBase();
        double balance = 0;
        String sql = "SELECT BALANCE FROM CUSTOMER WHERE PHONE_NUMBER = '"+ this.phoneNumber + "'";
        ResultSet queryResult = db.query(sql);
        while (queryResult.next()) {
            balance = queryResult.getDouble("BALANCE");
        }
        return balance;
    }

    public void pay() throws SQLException{
        finalizeCart();
        if(getBalance() < getTotalMoney()){
            System.out.println();
            System.out.println("Your balance is insufficient to check out. Please top up!");
            topUp();
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        Database db = Database.getDataBase();
        String sql = "UPDATE CUSTOMER SET BALANCE = '" + (getBalance() - getTotalMoney())+"' WHERE PHONE_NUMBER = '"+ this.phoneNumber + "'";
        try{
            db.update(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void proceedOrNot() throws SQLException{
        InputScanner scanner = new InputScanner();
        if (scanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (scanner.getInput().equals("Y")) {
            return;
        }
        if(!scanner.getInput().equals("B") && !scanner.getInput().equals("Y")){
            System.out.println("Please enter only B or Y");
            proceedOrNot();
        }
    }

    public void finalizeCart() throws SQLException{
        System.out.println("Before you pay, please check whether these items are what you want to purchase.");
        showShoppingCart();
        System.out.println("Now, would you make any changes before you pay?");
        System.out.println();
        System.out.println("    [A] Add a product to your shopping cart.");
        System.out.println("    [D] Delete a product from your shopping cart");
        System.out.println("    [N] No, I am ready to check out.");
        System.out.println();
        System.out.print("Please enter your choice: ");
        InputScanner scanner = new InputScanner();
        if(scanner.getInput().equals("A")){
            CustomerAddProductExecutor customerAddProductExecutor = new CustomerAddProductExecutor(this.phoneNumber);
            customerAddProductExecutor.executeInstruction();
        }
        if(scanner.getInput().equals("D")){
            CustomerDeleteProductExecutor customerDeleteProductExecutor = new CustomerDeleteProductExecutor(this.phoneNumber);
            customerDeleteProductExecutor.executeInstruction();
        }
        if(scanner.getInput().equals("N")){
            return;
        }
        if(!scanner.getInput().equals("A") && !scanner.getInput().equals("D") && !scanner.getInput().equals("N")){
            System.out.println();
            System.out.println("Invalid input. Please enter only A, D, or N.");
            finalizeCart();
        }
    }

    public  void showShoppingCart() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
        ResultSet shoppingCart = db.query(sql);
        System.out.println();
        System.out.println("Here is the information of your shopping cart.");
        while(shoppingCart.next()){
            String productID = shoppingCart.getString("PRODUCT_ID");
            String productName = shoppingCart.getString("PRODUCT_NAME");
            int quantity = shoppingCart.getInt("QUANTITY");
            double singlePrice = shoppingCart.getDouble("SINGLE_PRICE");
            double totalPrice = shoppingCart.getDouble("TOTAL_PRICE");
            System.out.println("PRODUCT_ID: "+productID+", PRODUCT_NAME: "+productName+", QUANTITY: "+quantity+", SINGLE_PRICE: "+singlePrice+", TOTAL_PRICE: "+totalPrice);
        }
    }
    public void topUp() throws SQLException{
        System.out.println();
        System.out.println("You are now adding value to your account.");
        System.out.print("Please input the amount of money you want to top up: ");
        InputScanner scanner = new InputScanner();
        double amount = Double.parseDouble(scanner.getInput());
        Database db = Database.getDataBase();
        String sql = "UPDATE CUSTOMER SET BALANCE = '" + (getBalance() + amount)+"' WHERE PHONE_NUMBER = '"+ this.phoneNumber + "'";
        try{
            db.update(sql);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Your account has been successfully topped up!");
        System.out.println("Your balance is now "+getBalance());
    }

    public void generateOrder() throws SQLException {
        System.out.println();
        System.out.println("Here are the details of your order");
        Database db = Database.getDataBase();

        String getSC = "SELECT * FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
        ResultSet shoppingCart = db.query(getSC);
        String[] productID = getProductID();
        int[] quantity = getQuantity();
        double[] cost = getCost();
        int i = 0;
        while (shoppingCart.next()) {
            this.orderID = setOrderID();
            String sql = "INSERT INTO ORDER_ (ORDER_ID, CUSTOMER_PHONE_NUMBER, PRODUCT_ID, QUANTITY, COST, SHIPPING_ADDRESS) VALUES ('" + this.orderID + "','" + this.phoneNumber + "','" +productID[i]+"','"+quantity[i]+"','"+cost[i]+ "', '" + this.shippingAddress + "')";
            db.insert(sql);
        }
        printOrder();
    }

    public void setShippingAddress() throws SQLException{
        System.out.println();
        System.out.print("Please set your shipping address: ");
        InputScanner scanner = new InputScanner();
        if (!isValidShippingAddress(scanner.getInput().trim())) {
            System.out.println();
            System.out.println("The number of characters of the shipping address should not be more than 100");
            System.out.println("The address should also not contain quotation mark");
            setShippingAddress();
        }
        this.shippingAddress = scanner.getInput().trim();
        System.out.println();
        System.out.println("Your shipping address has been successfully set.");
        System.out.println("Do you want to change the address?");
        System.out.println();
        System.out.println("    [Y] Yes");
        System.out.println("    [N] No");
        System.out.println();
        System.out.print("Please enter your choice: ");
        checkAddressDecision(scanner.getInput());
    }

    public void changeShippingAddress(String oldAddress) throws SQLException{
        System.out.println();
        System.out.println("You are now changing your shipping address");
        System.out.println("The previous address is " + oldAddress);
        System.out.print("Please enter a new address: ");
        InputScanner scanner = new InputScanner();
        if (!isValidShippingAddress(scanner.getInput().trim())) {
            System.out.println();
            System.out.println("The number of characters of the shipping address should not be more than 100");
            System.out.println("The address should also not contain quotation mark");
            changeShippingAddress(oldAddress);
        }
        this.shippingAddress = scanner.getInput().trim();
        System.out.println("A new address has been set.");
        System.out.println("Do you want to continue changing your address?");
        System.out.println("    [Y] Yes");
        System.out.println("    [N] No");
        System.out.println();
        System.out.print("Please enter your choice: ");
        checkAddressDecision(scanner.getInput());
    }

    public void selectPresetShippingAddress() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C FROM CUSTOMER WHERE PHONE_NUMBER ='"+this.phoneNumber+"'";
        String addressA = null, addressB = null, addressC = null ;

        ResultSet addressSet = db.query(sql);
        if(addressSet.next()) {
            addressA = addressSet.getString("SHIPPING_ADDRESS_A");
            addressB = addressSet.getString("SHIPPING_ADDRESS_B");
            addressC = addressSet.getString("SHIPPING_ADDRESS_C");
        }
        if (isCertainAddressSet("SHIPPING_ADDRESS_A")) {
            if (isCertainAddressSet("SHIPPING_ADDRESS_B")) {
                if (isCertainAddressSet("SHIPPING_ADDRESS_C")) {
                    System.out.println("You have preset 3 shipping addresses.");
                    printAllAddresses();
                    System.out.println("If you want to use your preset address, please select one.");
                    System.out.println();
                    System.out.println("    [A] Shipping address A");
                    System.out.println("    [B] Shipping address B");
                    System.out.println("    [C] Shipping address C");
                    System.out.println();
                    System.out.print("Please enter your choice : ");
                    InputScanner scanner = new InputScanner();
                    if (scanner.getInput().trim().equals("A")) {
                        this.shippingAddress = addressA;
                    }
                    else if(scanner.getInput().trim().equals("B")){
                        this.shippingAddress = addressB;
                    }
                    else if(scanner.getInput().trim().equals("C")){
                        this.shippingAddress = addressC;
                    }

                    else{System.out.println("Invalid input. Try again!");
                        selectPresetShippingAddress();}

                }
                else{
                    System.out.println("You have preset 2 shipping addresses.");
                    printAllAddresses();
                    System.out.println("If you want to use your preset address, please select one.");
                    System.out.println();
                    System.out.println("    [A] Shipping address A");
                    System.out.println("    [B] Shipping address B");
                    System.out.println();
                    System.out.print("Please enter your choice: ");
                    InputScanner scanner = new InputScanner();
                    if (scanner.getInput().equals("A")) {
                        this.shippingAddress = addressA;
                    }
                    if(scanner.getInput().equals("B")){
                        this.shippingAddress = addressB;
                    }
                    else{
                        System.out.println("Invalid input. Try again!");
                        selectPresetShippingAddress();
                    }
                }
            }
            else{
                System.out.println("You have preset 1 shipping address.");
                printAllAddresses();
                System.out.println("If you want to use your preset address, please select one.");
                System.out.println("    [A] Shipping address A");
                InputScanner scanner = new InputScanner();
                if (scanner.getInput().equals("A")) {
                    this.shippingAddress = addressA;
                }
                else{
                    System.out.println("Invalid input. Try again!");
                    selectPresetShippingAddress();
                }
            }
        }
        else{
            System.out.println("You have not preset any address. Selecting preset address failed.");
            System.out.println("Please preset your address first by pressing B or Q (back or quit)");
            backOrQuit();
        }
        generateOrder();
        clearShoppingCart();
        System.out.println();
        System.out.println("The transaction is successful!");
        System.out.println("Your balance has now become " + getBalance());
        System.out.println();
        System.out.print("Press any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    public void checkAddressDecision(String a) throws SQLException{
        InputScanner input = new InputScanner();
        if(input.getInput().equals("Y")){
            changeShippingAddress(a);
        }
        if(input.getInput().equals("N")){
            System.out.println("You have chosen to not change your address.");
            System.out.println("You are directed to see your order.");
            generateOrder();
            clearShoppingCart();
            System.out.println();
            System.out.println("The transaction is successful!");
            System.out.println("Your balance has now become " + getBalance());
            System.out.println();
            System.out.print("Press any key to continue: ");
            InputScanner confirmationScanner = new InputScanner();
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if(!input.getInput().equals("Y") && !input.getInput().equals("N")){
            System.out.println();
            System.out.println("Invalid input, please enter only Y or N.");
            checkAddressDecision(a);
        }
    }

    public String setOrderID() throws SQLException{
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);
        String orderID =Long.toString(Math.abs(random.nextLong()));
        if (orderID.length() > 20) {
            return orderID.substring(0, 20);
        } else {
            return orderID;
        }
    }

    public String[] getProductID () throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT PRODUCT_ID FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"'";
        ResultSet resultSet = db.query(sql);
        String[] productID = (String[]) sqlResultToArray(resultSet);
        return productID;
    }

    public int[] getQuantity() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT QUANTITY FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"'";
        ResultSet resultSet = db.query(sql);
        Object[] quantity = sqlResultToArray(resultSet);
        int[] intArray = new int[quantity.length];
        for (int i = 0; i < quantity.length; i++) {
            intArray[i] = Integer.parseInt((String) quantity[i]);
        }
        return intArray;
    }

    public double[] getCost() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT TOTAL_PRICE FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"'";
        ResultSet resultSet = db.query(sql);
        Object[] cost = sqlResultToArray(resultSet);
        double[] doubleArray = new double[cost.length];
        for (int i = 0; i < cost.length; i++) {
            doubleArray[i] = Double.parseDouble((String) cost[i]);
        }
        return doubleArray;
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

    public void printOrder() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM ORDER_ WHERE ORDER_ID = '" + this.orderID + "'";
        ResultSet order = db.query(sql);
        System.out.println();
        while(order.next()){
            String orderID = order.getString("ORDER_ID");
            String productID = order.getString("PRODUCT_ID");
            int quantity = order.getInt("QUANTITY");
            double cost = order.getDouble("COST");
            String shippingAddress = order.getString("SHIPPING_ADDRESS");
            System.out.println();
            System.out.println("ORDER_ID: "+orderID+", PRODUCT_ID: "+productID+", QUANTITY: "+quantity+", COST: "+cost+", SHIPPING_ADDRESS: "+shippingAddress);
        }
        System.out.println();
    }

    public static boolean isValidShippingAddress (String shippingAddress) {
        if (!noQuotationMark(shippingAddress)) {
            return false;
        }
        return shippingAddress.length() <= 100;
    }

    public void clearShoppingCart() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "DELETE FROM SHOPPING_CART WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
        db.delete(sql);
        System.out.println();
        System.out.println("Your shopping cart has been cleared");
    }

    public void updateAvailableQuantity() throws SQLException {
        if (checkQuantity()) {
            String sql = "UPDATE PRODUCT p " +
                    "SET p.AVAILABLE_QUANTITY = (p.AVAILABLE_QUANTITY - (SELECT c.QUANTITY FROM SHOPPING_CART c WHERE c.PRODUCT_ID = p.PRODUCT_ID AND c.CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'))"
                    + "WHERE EXISTS (SELECT 1 FROM SHOPPING_CART c WHERE c.PRODUCT_ID = p.PRODUCT_ID AND c.CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "')";
            try {
                Database db = Database.getDataBase();
                db.update(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }  else {
            System.out.println("Insufficient quantity!");
        }
    }

    public boolean checkQuantity() throws SQLException {
        Database db = Database.getDataBase();
        try {
            String sql = "SELECT c.PRODUCT_ID, c.PRODUCT_NAME, c.QUANTITY, p.AVAILABLE_QUANTITY " +
                    "FROM SHOPPING_CART c " +
                    "JOIN PRODUCT p ON c.PRODUCT_ID = p.PRODUCT_ID " +
                    "WHERE c.CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                int cartQuantity = rs.getInt("QUANTITY");
                int availableQuantity = rs.getInt("AVAILABLE_QUANTITY");
                String productId = rs.getString("PRODUCT_ID");
                String productName = rs.getString("PRODUCT_NAME");
                if (cartQuantity > availableQuantity) {
                    System.out.println();
                    System.out.println("Product ID: " + productId + " Product Name: " + productName + " has insufficient quantity!");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public void printAllAddresses() throws SQLException{
        System.out.println();
        System.out.println("Here are the details of your shipping address");
        System.out.println();
        Database db = Database.getDataBase();
        String sql = "SELECT SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C FROM CUSTOMER WHERE PHONE_NUMBER ='"+this.phoneNumber+"'";
        try{
            ResultSet addressSet = db.query(sql);
            while(addressSet.next()){
                String addressA = addressSet.getString("SHIPPING_ADDRESS_A");
                String addressB = addressSet.getString("SHIPPING_ADDRESS_B");
                String addressC = addressSet.getString("SHIPPING_ADDRESS_C");
                System.out.println("SHIPPING_ADDRESS_A: "+addressA+"\nSHIPPING_ADDRESS_B: "+addressB+"\nSHIPPING_ADDRESS_C: "+addressC);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public boolean isCertainAddressSet(String address) throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT " + address + " FROM CUSTOMER WHERE PHONE_NUMBER ='" + this.phoneNumber + "'";
        try {
            ResultSet resultSet = db.query(sql);
            if (resultSet.next()) {
                String addressValue = resultSet.getString(address);
                return !(addressValue == null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void backOrQuit() throws SQLException{
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
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
        else {
            System.out.println("Invalid input. Try again!");
            backOrQuit();
        }
    }

    public void checkChoice() throws SQLException{
        System.out.print("Please enter your choice: ");
        InputScanner choice = new InputScanner();
        if(choice.getInput().equals("1")){
            setShippingAddress();
        }
        if(choice.getInput().equals("2")){
            selectPresetShippingAddress();
        }
        else{
            System.out.println("Invalid input. Try again!");
            checkChoice();
        }
    }

}