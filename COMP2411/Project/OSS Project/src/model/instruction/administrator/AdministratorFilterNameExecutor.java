package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.OSS.noQuotationMark;
import static model.instruction.administrator.AdministratorDeleteProductExecutor.showResultSetInformation;

public class AdministratorFilterNameExecutor implements InstructionExecutor {

    private String phoneNumber;
    private String keyword;

    public AdministratorFilterNameExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now filtering with a keyword on the product name");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
        System.out.print("Please enter your keyword (no more than 20 characters): ");
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        this.keyword = getKeyword();
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
                "WHERE p.PRODUCT_NAME LIKE '%" + this.keyword + "%'";
        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_NAME LIKE '%" + this.keyword + "%'";
        ResultSet rs = db.query(sss);
        showResultSetInformation(rs);
    }

    public String getKeyword() throws SQLException {
        InputScanner keywordScanner = new InputScanner();
        if (keywordScanner.getInput().equals("B")) {
            AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
            administratorFilterExecutor.executeInstruction();
        }
        if (keywordScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String keyword = keywordScanner.getInput().trim();
        if (keyword.length() > 20) {
            System.out.print("Invalid input! Please input again: ");
            return getKeyword();
        }
        if (!noQuotationMark(keyword)) {
            System.out.print("The keyword can not contain any quotation mark! Please input again: ");
            return getKeyword();
        }
        return keyword;
    }

}
