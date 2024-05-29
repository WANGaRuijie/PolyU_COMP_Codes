package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerPresetShippingAddressExecutor implements InstructionExecutor {

    private final String phoneNumber;
    public CustomerPresetShippingAddressExecutor(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information(){
        System.out.println("*********************************************************************************");
        System.out.println("You are now presetting your shipping address.");
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

    public void confirm() throws SQLException {
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
    public void visitDatabase() throws SQLException{
        if(addressNotSet()){
            System.out.println("You haven't preset any shipping address. Do you want to set it now ?");
            System.out.println();
            System.out.println("    [B] Back");
            System.out.println("    [Q] Quit");
            System.out.println("    [Any other key] Yes");
            System.out.println();
            confirm();
            System.out.println();
            System.out.println("You can preset up to 3 shipping addresses");
            setAddress1();
            System.out.println();
            System.out.println("Do you want to continue to set your second address?");
            confirm();
            setAddress2();
            System.out.println();
            System.out.println("Do you want to continue to set your third address?");
            confirm();
            setAddress3();
            System.out.println();
            System.out.println("You have preset 3 addresses and no more address can be preset");
            backOrQuit();
        }
        else{
            if(!isCertainAddressSet("SHIPPING_ADDRESS_B")){
                System.out.println();
                System.out.println("You just preset your first address. Do you want to set your second address?");
                confirm();
                setAddress2();
                System.out.println("Do you want to continue to set your third address?");
                confirm();
                setAddress3();
                backOrQuit();
            }
            else{
                if(!isCertainAddressSet("SHIPPING_ADDRESS_C")){
                    System.out.println();
                    System.out.println("You just preset your first and second addresses. Do you want to set your third address?");
                    confirm();
                    setAddress3();
                    backOrQuit();
                }
                else{
                    System.out.println();
                    System.out.println("You have already preset 3 shipping addresses");
                    backOrQuit();
                }
            }
        }
    }

    public boolean addressNotSet() throws SQLException{
        Database db = Database.getDataBase();
        String sql = "SELECT * FROM CUSTOMER WHERE PHONE_NUMBER ='"+this.phoneNumber+"'";
        try{
            ResultSet addressSet = db.query(sql);
            String address_a = null;
            if (addressSet.next()) {
                 address_a = addressSet.getString("SHIPPING_ADDRESS_A");
            }
            if (address_a == null) {
                return true;
            }
            return false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void setAddress1() throws SQLException{
        System.out.println();
        System.out.print("Now, please input your first address: ");
        InputScanner address1 = new InputScanner();
        if (address1.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (address1.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String setAddress1 = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_A = '"+address1.getInput()+"'";
        Database db = Database.getDataBase();
        try{
            db.update(setAddress1);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Your first shipping address has been successfully set");
    }

    public void setAddress2() throws SQLException{
        System.out.println();
        System.out.print("Now, please input your second address: ");
        InputScanner address2 = new InputScanner();
        if (address2.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (address2.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String setAddress2 = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_B = '"+address2.getInput()+"'";
        Database db = Database.getDataBase();
        try{
            db.update(setAddress2);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Your second shipping address has been successfully set.");
    }

    public void setAddress3() throws SQLException{
        System.out.println();
        System.out.print("Now, please input your third address: ");
        InputScanner address3 = new InputScanner();
        if (address3.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (address3.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String setAddress3 = "UPDATE CUSTOMER SET SHIPPING_ADDRESS_C = '"+address3.getInput()+"'";
        Database db = Database.getDataBase();
        try{
            db.update(setAddress3);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Your third shipping address has been successfully set");
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


}





