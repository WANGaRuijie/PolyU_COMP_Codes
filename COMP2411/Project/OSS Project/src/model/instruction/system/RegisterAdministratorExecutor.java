package model.instruction.system;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.OSS;

import java.sql.SQLException;

public class RegisterAdministratorExecutor implements InstructionExecutor {

    private String phoneNumber;
    private String userName;
    private String password;

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now creating a new administrator account");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {

        System.out.print("Please enter your phone number (no more than 20 digits): ");
        this.phoneNumber = getPhoneNumber();
        System.out.print("Please enter your user name (no more than 20 characters and only contains letters, digits and underlines): ");
        this.userName = getUsername();
        System.out.print("Please enter your account password (no more than 20 characters): ");
        this.password = getPassword();
        visitDatabase();
        System.out.println();
        System.out.println("Administrator account registered successfully");
        OSS newOSS = new OSS();
        newOSS.activate();
    }

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "INSERT INTO ADMIN (PHONE_NUMBER, USER_NAME, PASSWORD) VALUES ('" + this.phoneNumber + "', '" + this.userName + "', '" + this.password +  "')";
        try {
            db.insert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void choose(String input) {}

    public String getPhoneNumber() throws SQLException {
        InputScanner phoneNumberScanner = new InputScanner();
        if (phoneNumberScanner.getInput().equals("B")) {
            OSS newOSS = new OSS();
            newOSS.activate();
        }
        if (phoneNumberScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String phoneNumber = phoneNumberScanner.getInput().trim();
        if (!isValidPhoneNumber(phoneNumber)) {
            System.out.print("Invalid phone number! Please input again: " );
            return getPhoneNumber();
        }
        return phoneNumber;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) throws SQLException {

        if (phoneNumber.length() > 20) {
            return false;
        }

        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                return false;
            }
        }

        if (isRepeatedPhoneNumber(phoneNumber)) {
            System.out.println("The phone number has been registered");
            return false;
        }

        return true;

    }

    public String getUsername() throws SQLException {
        InputScanner usernameScanner = new InputScanner();
        if (usernameScanner.getInput().equals("B")) {
            OSS newOSS = new OSS();
            newOSS.activate();
        }
        if (usernameScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String username = usernameScanner.getInput().trim();
        if (!isValidUsername(username)) {
            System.out.print("Invalid username! Please input again: ");
            return getUsername();
        }

        return username;
    }

    public static boolean isValidUsername(String username) {

        if (username.length() > 20) {
            return false;
        }

        for (int i = 0; i < username.length(); i++) {
            char ch = username.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '_') {
                return false;
            }
        }

        return true;

    }

    public static boolean isRepeatedPhoneNumber(String phoneNumber) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("ADMIN", "PHONE_NUMBER", phoneNumber);
    }

    public String getPassword() throws SQLException {
        InputScanner passwordScanner = new InputScanner();
        if (passwordScanner.getInput().equals("B")) {
            OSS newOSS = new OSS();
            newOSS.activate();
        }
        if (passwordScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String password = passwordScanner.getInput().trim();
        if (!isValidPassword(password)) {
            System.out.print("Invalid input! Please input again: ");
            return getPassword();
        }

        return password;
    }

    public static boolean isValidPassword(String password) {
        return password.length() <= 20;
    }

}
