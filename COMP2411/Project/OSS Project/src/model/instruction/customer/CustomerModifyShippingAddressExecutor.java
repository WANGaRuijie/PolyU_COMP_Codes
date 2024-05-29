package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModifyShippingAddressExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public CustomerModifyShippingAddressExecutor(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information(){
        System.out.println("*********************************************************************************");
        System.out.println("You are now modifying your shipping address.");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void choose(String input) throws SQLException{}

    @Override
    public void executeInstruction() throws SQLException{
        information();
        confirm();
        visitDatabase();
    }

    @Override
    public void visitDatabase() throws SQLException{
        printAllAddresses();
        modifyAddress1();
        confirm();
        modifyAddress2();
        confirm();
        modifyAddress3();
        backOrQuit();
    }

    public void confirm() throws SQLException{
        System.out.println();
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

    public void printAllAddresses() throws SQLException{
        System.out.println();
        System.out.println("Here are the details of your shipping address");
        Database db = Database.getDataBase();
        String sql = "SELECT SHIPPING_ADDRESS_A, SHIPPING_ADDRESS_B, SHIPPING_ADDRESS_C FROM CUSTOMER WHERE PHONE_NUMBER ='"+this.phoneNumber+"'";
        try{
            ResultSet addressSet = db.query(sql);
            while(addressSet.next()){
                String addressA = addressSet.getString("SHIPPING_ADDRESS_A");
                String addressB = addressSet.getString("SHIPPING_ADDRESS_B");
                String addressC = addressSet.getString("SHIPPING_ADDRESS_C");
                System.out.println();
                System.out.println("SHIPPING_ADDRESS_A: "+addressA+"\nSHIPPING_ADDRESS_B: "+addressB+"\nSHIPPING_ADDRESS_C: "+addressC);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void modifyAddress1() throws SQLException{
        if(!isCertainAddressSet("SHIPPING_ADDRESS_A")){
            System.out.println();
            System.out.println("You have not preset any address. Modification failed. Please preset your address first");
            backOrQuit();
        }
        else{
            System.out.println();
            System.out.print("Please enter a new address for your first address: ");
            InputScanner newAddress1 = new InputScanner();
            Database db = Database.getDataBase();
            String sql = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_A = '"+newAddress1.getInput()+"' WHERE PHONE_NUMBER = '"+this.phoneNumber+"'";
            try{
                db.update(sql);
                System.out.println("The first address has been successfully modified.");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void modifyAddress2() throws SQLException{
        if(!isCertainAddressSet("SHIPPING_ADDRESS_B")){
            System.out.println();
            System.out.println("You have not preset your second address. Modification failed. Please preset your second address first");
            backOrQuit();
        }
        else{
            System.out.println();
            System.out.print("Please enter a new address for your second address: ");
            InputScanner newAddress2 = new InputScanner();
            Database db = Database.getDataBase();
            String sql = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_B = '"+newAddress2.getInput()+"' WHERE PHONE_NUMBER = '"+this.phoneNumber+"'";
            try{
                db.update(sql);
                System.out.println();
                System.out.println("The second address has been successfully modified.");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void modifyAddress3() throws SQLException{
        if(!isCertainAddressSet("SHIPPING_ADDRESS_C")){
            System.out.println();
            System.out.println("You have not preset your third address. Modification failed. Please preset your third address first");
            backOrQuit();
        }
        else{
            System.out.println();
            System.out.print("Please enter a new address for your third address: ");
            InputScanner newAddress3 = new InputScanner();
            Database db = Database.getDataBase();
            String sql = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_C = '"+newAddress3.getInput()+"' WHERE PHONE_NUMBER = '"+this.phoneNumber+"'";
            try{
                db.update(sql);
                System.out.println();
                System.out.println("The third address has been successfully modified");
            }catch(Exception e){
                e.printStackTrace();
            }
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
        System.out.print("Please enter your choice: ");
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
            System.out.println();
            System.out.println("Invalid input. Try again!");
            backOrQuit();
        }
    }


}
