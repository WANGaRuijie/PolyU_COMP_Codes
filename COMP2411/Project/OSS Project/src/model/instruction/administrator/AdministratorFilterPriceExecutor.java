package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.instruction.administrator.AdministratorAddProductExecutor.isValidPriceInput;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorFilterPriceExecutor implements InstructionExecutor {

    private String phoneNumber;
    private double numberA;
    private double numberB;

    public AdministratorFilterPriceExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now filtering with a criterion on the product price ");
        System.out.println();
        System.out.println("You are expected to enter two numbers as the both ends of the price range (both will be inclusive)");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        System.out.print("Please input the value of the smaller end: ");
        this.numberA = Double.parseDouble(getNumberA());
        System.out.print("Please input the value of the larger end: ");
        this.numberB = Double.parseDouble(getNumberB());
        if (this.numberA > this.numberB) {
            System.out.println("The value of the smaller end is larger than the larger end!");
            System.out.println("Please input again!");
            executeInstruction();
        }
        visitDatabase();
        System.out.println();
        System.out.println("The query result is printed");
        System.out.println();
        System.out.print("Enter any key to continue: ");
        InputScanner confirmationScanner = new InputScanner();
        AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
        administratorFilterExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sss = "SELECT p.PRODUCT_ID, p.PRODUCT_NAME, p.DESCRIPTION, p.SPECIFICATION, p.PRICE, p.AVAILABLE_QUANTITY, p.CATEGORY, r.CONTENT " +
                "FROM PRODUCT p " +
                "LEFT JOIN REVIEW r ON p.PRODUCT_ID = r.PRODUCT_ID " +
                "WHERE (p.PRICE > " + this.numberA + ") AND (p.PRICE < " + this.numberB + ")";
        String sql = "SELECT * FROM PRODUCT WHERE (PRICE > " + this.numberA + ") AND (PRICE < " + this.numberB + ")";
        ResultSet rs = db.query(sss);
        showResultSetInformation(rs);
    }

    public String getNumberA() throws SQLException {
        InputScanner numberScanner = new InputScanner();
        if (numberScanner.getInput().equals("B")) {
            AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
            administratorFilterExecutor.executeInstruction();
        }
        if (numberScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String numberA = numberScanner.getInput().trim();
        if (!isValidPriceInput(numberA)) {
            System.out.print("Invalid input! Please input again: ");
            return getNumberA();
        }
        return numberA;
    }

    public String getNumberB() throws SQLException {
        InputScanner numberScanner = new InputScanner();
        if (numberScanner.getInput().equals("B")) {
            AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
            administratorFilterExecutor.executeInstruction();
        }
        if (numberScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String numberB = numberScanner.getInput().trim();
        if (!isValidPriceInput(numberB)) {
            System.out.print("Invalid input! Please input again: ");
            return getNumberA();
        }
        return numberB;
    }

}