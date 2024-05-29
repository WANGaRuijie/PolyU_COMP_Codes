package model.instruction.customer;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.administrator.AdministratorPanelExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerTopUpExecutor implements InstructionExecutor {

    private final String phoneNumber;

    public CustomerTopUpExecutor(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now adding value to your account.");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        visitDatabase();
        CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
        customerPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException{}

    @Override
    public void visitDatabase() throws SQLException{
        System.out.print("Please input the amount of money you want to top up: ");
        InputScanner scanner = new InputScanner();
        double amount = getAmount();
        Database db = Database.getDataBase();
        String sql = "UPDATE CUSTOMER SET BALANCE = '" + (getBalance()+amount)+"' WHERE PHONE_NUMBER = '"+ this.phoneNumber + "'";
        try{
            db.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println("Your account has been successfully topped up!");
        System.out.println("Your balance is now " + getBalance());
    }

    public double getBalance(){
        Database db = Database.getDataBase();
        double balance = 0;
        String sql = "SELECT BALANCE FROM CUSTOMER WHERE PHONE_NUMBER = '"+ this.phoneNumber + "'";
        try {
            ResultSet queryResult = db.query(sql);
            while (queryResult.next()) {
                balance = queryResult.getDouble("BALANCE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    public double getAmount() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String amount = productIDScanner.getInput().trim();
        if (!isValidAmount(amount)) {
            System.out.print("The input is not valid! Please input again: ");
            return getAmount();
        }
        return Double.parseDouble(amount);
    }

    public static boolean isValidAmount(String amount) {
        try {
            Double.parseDouble(amount);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
