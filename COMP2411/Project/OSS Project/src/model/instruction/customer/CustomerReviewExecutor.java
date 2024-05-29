package model.instruction.customer;
import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerReviewExecutor implements InstructionExecutor {
    private final String phoneNumber;
    private String orderID;
    public CustomerReviewExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information(){
        System.out.println("*********************************************************************************");
        System.out.println("You are now giving review to your purchase");
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
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();

    }
    @Override
    public void choose(String input) throws SQLException {

    }

    public void confirm() throws SQLException{
        System.out.print("If you don't want to quit or go back, press any key except B and Q: ");
        InputScanner scanner = new InputScanner();
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
    public void visitDatabase() throws SQLException{
        System.out.println();
        System.out.println("Here is the information of all your orders");
        printOrder();
        giveReview();

    }
    public void printOrder() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM ORDER_ WHERE CUSTOMER_PHONE_NUMBER = '" + this.phoneNumber + "'";
        ResultSet order = db.query(sql);
        System.out.println();
        while(order.next()){
            String orderID = order.getString("ORDER_ID");
            String productID = order.getString("PRODUCT_ID");
            int quantity = order.getInt("QUANTITY");
            double cost = order.getDouble("COST");
            String shippingAddress = order.getString("SHIPPING_ADDRESS");
            System.out.println("ORDER_ID: "+ orderID +", PRODUCT_ID: "+ productID +", QUANTITY: "+ quantity +", COST: " + cost + ", SHIPPING_ADDRESS: " + shippingAddress);
        }
    }

    public void giveReview() throws SQLException{
        Database db = Database.getDataBase();
        System.out.println();
        System.out.println("Please enter the corresponding ORDER_ID that you want to review");
        InputScanner scanner = new InputScanner();
        if (scanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (scanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if (!isExistedOrderID(scanner.getInput())){
            reEnterInputID();
        }
        if (isExistedOrderID(scanner.getInput().trim()) && (!isRepeatedReview(scanner.getInput().trim()))) {
            String productID = getProductID(scanner.getInput());
            System.out.println();
            System.out.print("Please enter your comment: ");
            InputScanner comment = new InputScanner();
            if (comment.getInput().equals("B")) {
                CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
                customerPanelExecutor.executeInstruction();
            }
            if (comment.getInput().equals("Q")) {
                QuitExecutor quitExecutor = new QuitExecutor();
                quitExecutor.executeInstruction();
            }
            String sql = "INSERT INTO REVIEW(ORDER_ID, CUSTOMER_PHONE_NUMBER, PRODUCT_ID, CONTENT) VALUES('" + scanner.getInput().trim() + "','" + this.phoneNumber + "','"+ productID +"','"+comment.getInput()+"')";
            db.insert(sql);
            System.out.println();
            System.out.println("Your comment has been successfully added");
        }
        if (isExistedOrderID(scanner.getInput().trim()) && (isRepeatedReview(scanner.getInput().trim()))) {
            System.out.println();
            System.out.println("You have review the order. Do you want to review it again and cover the old one?");
            System.out.println();
            System.out.println("    [Y] Yes");
            System.out.println("    [N] No");
            System.out.println();
            System.out.print("Please enter your choice: ");
            InputScanner choiceScanner = new InputScanner();
            if (choiceScanner.getInput().trim().equals("N")) {
                CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
                customerPanelExecutor.executeInstruction();
            }
            System.out.print("Please enter your comment: ");
            InputScanner comment = new InputScanner();
            if (comment.getInput().equals("B")) {
                CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
                customerPanelExecutor.executeInstruction();
            }
            if (comment.getInput().equals("Q")) {
                QuitExecutor quitExecutor = new QuitExecutor();
                quitExecutor.executeInstruction();
            }
            String sql = "UPDATE REVIEW SET CONTENT = '" + comment.getInput().trim() + "' WHERE ORDER_ID = '" + scanner.getInput().trim() + "'";
            db.update(sql);
            System.out.println("Your comment has been successfully updated");
        }
    }

    public void reEnterInputID() throws SQLException{
        System.out.println();
        System.out.print("Invalid orderID! Please input again: ");
        InputScanner scanner = new InputScanner();
        if (scanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (scanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if(!isExistedOrderID(scanner.getInput())){
            reEnterInputID();
        }
    }
    public boolean isExistedOrderID(String input) throws SQLException{
        String[] orderID = getOrderID();
        for (String s : orderID) {
            if (input.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public String[] getOrderID() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT ORDER_ID FROM ORDER_ WHERE CUSTOMER_PHONE_NUMBER = '"+this.phoneNumber+"'";
        ResultSet resultSet = db.query(sql);
        return (String[]) sqlResultToArray(resultSet);
    }

    public String getProductID(String order_id) throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT PRODUCT_ID FROM ORDER_ WHERE ORDER_ID = '"+order_id+"' ";
        ResultSet resultSet = db.query(sql);
        String[] productID = (String[]) sqlResultToArray(resultSet);
        return productID[0];
    }

    public  Object[] sqlResultToArray(ResultSet resultSet) throws SQLException {
        List<String> resultList = new ArrayList<>();
        while (resultSet.next()) {
            String a = resultSet.getString(1);
            resultList.add(a);
        }
        return resultList.toArray(new String[0]);
    }

    public boolean isRepeatedReview(String orderID) throws SQLException {
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM REVIEW WHERE ORDER_ID = '" + orderID +"'";
        try {
            ResultSet rs = db.query(sql);
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
