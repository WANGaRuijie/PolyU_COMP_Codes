package model.instruction.system;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.customer.CustomerPanelExecutor;

import java.sql.*;


public class LoginCustomerExecutor implements InstructionExecutor {

    private String phoneNumber;
    private String password;
    private boolean loggedIn = false;

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now logging in as a customer");
    }

    @Override
    public void executeInstruction() throws SQLException {
        while (!loggedIn) {
            information();
            System.out.println();
            System.out.print("Please enter your phone number: ");
            InputScanner phoneNumberScanner = new InputScanner();
            this.phoneNumber = phoneNumberScanner.getInput();
            System.out.print("Please enter your password: ");
            InputScanner passwordScanner = new InputScanner();
            this.password = passwordScanner.getInput();
            visitDatabase();

            if (loggedIn) {
                break;
            } else {
                System.out.println("Login failed. Enter any key to continue or enter 'B' to back");
                InputScanner choiceScanner = new InputScanner();
                String choice = choiceScanner.getInput();
                if (choice.equals("B")) {
                    break;
                }
            }
        }
        if (loggedIn) {
            System.out.println("Customer account logged in successfully");
            CustomerPanelExecutor customerPanelExecutor = new CustomerPanelExecutor(this.phoneNumber);
            customerPanelExecutor.executeInstruction();
        } else {
            LoginChoiceExecutor loginChoiceExecutor = new LoginChoiceExecutor();
            loginChoiceExecutor.executeInstruction();
        }
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        ResultSet resultSet;
        String sql = "SELECT PASSWORD FROM CUSTOMER WHERE PHONE_NUMBER = '" + this.phoneNumber + "'";
        try {
            resultSet = db.query(sql);
            if (resultSet.next()) {
                String dbPassword = resultSet.getString("PASSWORD").trim();
                if (dbPassword.equals(this.password)) {
                    System.out.println("Login successful");
                    this.loggedIn = true;
                } else {
                    System.out.println("Incorrect password!");
                }
            } else {
                System.out.println("Phone number not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
