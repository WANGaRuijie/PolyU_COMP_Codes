package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdministratorGenerateReportExecutor implements InstructionExecutor {

    private final String phoneNumber;
    private File file;

    public AdministratorGenerateReportExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() throws SQLException {
        System.out.println("*********************************************************************************");
        System.out.println("You are now generating an analysis report");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {
        information();
        this.file = getFile();
        generateReport();
        System.out.println();
        System.out.println("The analysis report has been successfully generated and stored");
        System.out.println();
        System.out.print("Press any key to continue: ");
        InputScanner scanner = new InputScanner();
        AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
        administratorPanelExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {}

    public File getFile() throws SQLException {

        System.out.print("Please input the filepath to store the analysis report: ");
        InputScanner filepathScanner = new InputScanner();

        if (filepathScanner.getInput().equals("B")) {
            AdministratorFilterExecutor administratorFilterExecutor = new AdministratorFilterExecutor(this.phoneNumber);
            administratorFilterExecutor.executeInstruction();
        }

        if (filepathScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }

        String filepath = filepathScanner.getInput().trim();

        File file = new File(filepath);
        if (file.exists()) {
            return file;
        } else if (file.isDirectory()) {
            File defaultFile = new File(filepath, "default.txt");
            try {
                if (defaultFile.createNewFile()) {
                    System.out.println("A default file is created under the given directory");
                    file = defaultFile;
                }
            } catch (IOException e) {
                System.out.println("Can not create a default file!");
                return getFile();
            }
        } else {
            System.out.println("Invalid filepath!");
            return getFile();
        }

        return file;
    }

    public void generateReport() {

        try (FileWriter writer = new FileWriter(this.file)) {

            String customerCount = getCustomerCount();
            writer.write("The total number of all registered customers: " + customerCount + "\n\n");

            String productCount = getProductCount();
            writer.write("The total number of all products: " + productCount +"\n\n");

            String orderCount = getOrderCount();
            writer.write("The total number of all orders: " + orderCount + "\n\n");

            List<String> result1 = getTheMostPopular();

            if (result1 != null) {

                writer.write("The 10 most popular products: \n\n");

                for (String s : result1) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.write("\n");
                writer.write("You can provide sales on these products!");
                writer.write("\n\n");
            } else {
                writer.write("No sufficient information for generating the 10 most popular products");
                writer.write("\n\n");
            }


            List<String> result2 = getTheLeastPopular();

            if (result1 != null) {
                writer.write("The 10 least popular product: \n\n");
                for (String s : result2) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.write("\n");
                writer.write("You can adjust the prices of these products!");
                writer.write("\n\n");
            } else {
                writer.write("No sufficient information for generating the 10 least popular products");
                writer.write("\n\n");
            }

            List<String> result3 = getBalanceAnalysis();

            if (result3 != null) {

                writer.write("Analysis about customer balances and product prices: \n\n");
                for (String s : result3) {
                    writer.write(s);
                    writer.write("\n");
                }
                writer.write("\n\n");
            } else {
                writer.write("No sufficient information for generating statistical analysis on customer balances and product prices");
                writer.write("\n\n");
            }

            writer.flush();

        } catch (IOException | SQLException e) {
            System.out.println("The file does not exist");
        }
    }

    public List<String> getTheMostPopular() throws SQLException {
        List<String> result = new ArrayList<>();
        String sql =
                "SELECT * FROM (SELECT o.PRODUCT_ID, s.PRODUCT_NAME, SUM(o.quantity) as total_quantity, RANK() OVER (ORDER BY SUM(o.quantity) DESC) as rn FROM ORDER_ o JOIN PRODUCT s ON o.PRODUCT_ID = s.PRODUCT_ID GROUP BY o.PRODUCT_ID, s.PRODUCT_NAME) WHERE rn <= 10";
        Database db = Database.getDataBase();
        try {
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                String mostPopularProductId = rs.getString("PRODUCT_ID");
                String productName = rs.getString("PRODUCT_NAME");
                int rank = rs.getInt("rn");
                result.add("Rank: " + rank + ", Product ID: " + mostPopularProductId + ", Product Name: " + productName);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getTheLeastPopular() {
        List<String> result = new ArrayList<>();
        String sql =
                "SELECT * FROM (SELECT o.PRODUCT_ID, s.PRODUCT_NAME, SUM(o.quantity) as total_quantity, RANK() OVER (ORDER BY SUM(o.quantity) ASC) as rn FROM ORDER_ o JOIN PRODUCT s ON o.PRODUCT_ID = s.PRODUCT_ID GROUP BY o.PRODUCT_ID, s.PRODUCT_NAME) WHERE rn <= 10";
        Database db = Database.getDataBase();
        try {
            ResultSet rs = db.query(sql);
            while (rs.next()) {
                String mostPopularProductId = rs.getString("PRODUCT_ID");
                String productName = rs.getString("PRODUCT_NAME");
                int rank = rs.getInt("rn");
                result.add("Rank (Last): " + rank + ", Product ID: " + mostPopularProductId + ", Product Name: " + productName);
            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getBalanceAnalysis() throws SQLException {

        List<String> result = new ArrayList<>();
        Database db = Database.getDataBase();

        List<Double> balances = new ArrayList<>();

        double sum1 = 0, mean1 = 0, median1 = 0, variance1 = 0, min1 = 0, max1 = 0;
        String sql1 = "SELECT BALANCE FROM CUSTOMER";

        List<Float> prices = new ArrayList<>();
        float sum = 0, mean = 0, median = 0, variance = 0, min = 0, max = 0;
        String sql = "SELECT PRICE FROM PRODUCT";

        try {

            ResultSet rs1 = db.query(sql1);
            while (rs1.next()) {
                double balance = rs1.getFloat("BALANCE");
                balances.add(balance);
                sum1 += balance;
            }
            int size1 = balances.size();
            if (size1 > 0) {
                mean1 = sum1 / size1;

                Collections.sort(balances);
                if (size1 % 2 == 0) {
                    median1 = (balances.get(size1 / 2 - 1) + balances.get(size1 / 2)) / 2;
                } else {
                    median1 = balances.get(size1 / 2);
                }

                for (double balance : balances) {
                    variance1 += Math.pow(balance - mean1, 2);
                }
                variance1 /= size1;

                min1 = Collections.min(balances);
                max1 = Collections.max(balances);

                result.add("Balance mean: " + mean1);
                result.add("Balance median: " + median1);
                result.add("Balance variance: " + variance1);
                result.add("Balance min: " + min1);
                result.add("Balance max: " + max1);
                result.add("\n");
            } else {
                result.add("No records found!");
            }


            ResultSet rs = db.query(sql);
            while (rs.next()) {
                float price = rs.getFloat("PRICE");
                prices.add(price);
                sum += price;
            }

            int size = prices.size();
            if (size > 0) {
                mean = sum / size;

                Collections.sort(prices);
                if (size % 2 == 0) {
                    median = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2;
                } else {
                    median = prices.get(size / 2);
                }

                for (float price : prices) {
                    variance += Math.pow(price - mean, 2);
                }
                variance /= size;

                min = Collections.min(prices);
                max = Collections.max(prices);

                result.add("Price mean: " + mean);
                result.add("Price median: " + median);
                result.add("Price variance: " + variance);
                result.add("Price min: " + min);
                result.add("Price max: " + max);
                result.add("\n");
            } else {
                result.add("No product records found!");
            }

            if (size > 0 && size1 > 0) {

                boolean findCondition = false;
                if (max1 < max || median1 < median || mean1 < mean) {
                    result.add("According to the statistics, it is beneficial if the price of the products can be deducted.");
                    findCondition = true;
                }
                if (median < mean) {
                    result.add("According to the statistics, it is beneficial if the price of the expensive products can be deducted.");
                    findCondition = true;
                }
                if (variance1 > variance) {
                    result.add("If the price distribution of products is significantly large, you might need to adjust the pricing of certain high cost items.");
                    findCondition = true;
                }
                if (mean1 < mean) {
                    result.add("If the average customer balance is low, consider providing options like installment payments for high cost items.");
                    findCondition = true;
                }
                if (median1 < mean1) {
                    result.add("According to the statistics, it is beneficial if the customers with low balance can be encouraged to top up");
                    findCondition = true;
                }
                if (!findCondition) {
                    result.add("According to the statistics, no valuable advice can be generated");
                }

            }  else {
                result.add("No sufficient data to give suggestions.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public  String getCustomerCount() throws SQLException {
        Database db = Database.getDataBase();
        int userCount = 0;
        try {
            String sql = "SELECT COUNT(*) FROM CUSTOMER";
            ResultSet rs = db.query(sql);
            if (rs.next()) {
                userCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "" + userCount;
    }

    public  String getProductCount() throws SQLException {
        Database db = Database.getDataBase();
        int productCount = 0;
        try {
            String sql = "SELECT COUNT(*) FROM PRODUCT";
            ResultSet rs = db.query(sql);
            if (rs.next()) {
                productCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "" + productCount;
    }

    public String getOrderCount() throws SQLException {
        Database db = Database.getDataBase();
        int orderCount = 0;
        try {
            String sql = "SELECT COUNT(*) FROM ORDER_";
            ResultSet rs = db.query(sql);
            if (rs.next()) {
                orderCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "" + orderCount;
    }

}