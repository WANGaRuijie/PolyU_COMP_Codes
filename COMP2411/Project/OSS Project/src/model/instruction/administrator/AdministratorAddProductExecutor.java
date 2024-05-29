package model.instruction.administrator;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.instruction.system.QuitExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;

import static model.OSS.noQuotationMark;

public class AdministratorAddProductExecutor implements InstructionExecutor {

    private String phoneNumber;

    private String productID;
    private String productName;
    private String description;
    private String specification;
    private double price;
    private int availableQuantity;
    private String category;

    public AdministratorAddProductExecutor(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("You are now adding a new product");
        System.out.println();
        System.out.println("    [B] Back");
        System.out.println("    [Q] Quit");
        System.out.println();
    }

    @Override
    public void executeInstruction() throws SQLException {

        information();
        System.out.print("Please enter the product ID of the new product (no more than 20 digits): ");
        this.productID = getProductID();
        System.out.print("Please enter the product name (no more than 20 characters and only contains letters, digits and underlines): ");
        this.productName = getProductName();
        System.out.print("Please enter the product description (no more than 200 characters and only contains letters, digits and underlines): ");
        this.description = getDescription();
        System.out.print("Please enter the product specification (no more than 200 characters and only contains letters, digits and underlines): ");
        this.specification = getSpecification();
        System.out.print("Please enter the product price (with the precision and scale of a double value): ");
        this.price = getPrice();
        System.out.print("Please enter the available quantity of the product (a positive integer): ");
        this.availableQuantity = getAvailableQuantity();
        System.out.print("Please enter the category of the product (no more than 20 characters): ");
        this.category = getCategory();

        visitDatabase();
        System.out.println();
        System.out.println("The new product is successfully added");
        AdministratorAddProductExecutor administratorAddProductExecutor = new AdministratorAddProductExecutor(this.phoneNumber);
        administratorAddProductExecutor.executeInstruction();
    }

    @Override
    public void choose(String input) throws SQLException {}

    @Override
    public void visitDatabase() throws SQLException {
        Database db = Database.getDataBase();
        String sql = "INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SPECIFICATION, PRICE, AVAILABLE_QUANTITY, CATEGORY) VALUES ('" + this.productID + "', '" + this.productName + "', '" + this.description + "', '" + this.specification + "', " + this.price + ", " + this.availableQuantity + ", '" + this.category + "')";
        try {
            db.insert(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProductID() throws SQLException {
        InputScanner productIDScanner = new InputScanner();
        if (productIDScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productIDScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String productID = productIDScanner.getInput().trim();
        if (!isValidProductID(productID)) {
            System.out.print("Invalid product ID! Please input again: ");
            return getProductID();
        }
        return productID;
    }

    public static boolean isValidProductID(String productID) throws SQLException {

        if (productID.length() > 20) {
            return false;
        }

        for (int i = 0; i < productID.length(); i++) {
            if (!Character.isDigit(productID.charAt(i))) {
                return false;
            }
        }

        if (isRepeatedProductID(productID)) {
            System.out.println("The product ID already exists");
            return false;
        }

        return true;
    }

    public static boolean isRepeatedProductID(String productID) throws SQLException {
        Database db = Database.getDataBase();
        return db.contains("PRODUCT", "PRODUCT_ID", productID);
    }

    public String getProductName() throws SQLException {
        InputScanner productNameScanner = new InputScanner();
        if (productNameScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (productNameScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String username = productNameScanner.getInput().trim();
        if (!isValidProductName(username)) {
            System.out.print("Invalid product name! Please input again: ");
            return getProductName();
        }

        return username;
    }

    public static boolean isValidProductName(String productName) throws SQLException {

        if (productName.length() > 20) {
            return false;
        }

        for (int i = 0; i < productName.length(); i++) {
            char ch = productName.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '_') {
                return false;
            }
        }

        return true;
    }

    public String getDescription() throws SQLException {
        InputScanner descriptionScanner = new InputScanner();
        if (descriptionScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (descriptionScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String description = descriptionScanner.getInput().trim();
        if (!isValidDescription(description)) {
            System.out.print("Invalid description! Please input again: ");
            return getDescription();
        }
        return description;
    }

    public static boolean isValidDescription(String description) {
        if (!noQuotationMark(description)) {
            System.out.println("Your input can not include quotation mark");
            return false;
        }
        return description.length() <= 200;
    }

    public String getSpecification() throws SQLException {
        InputScanner specificationScanner = new InputScanner();
        if (specificationScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (specificationScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        String specification = specificationScanner.getInput().trim();
        if (!isValidSpecification(specification)) {
            System.out.print("Invalid specification! Please input again: ");
            return getSpecification();
        }
        return specification;
    }

    public static boolean isValidSpecification(String specification) {
        if (!noQuotationMark(specification)) {
            System.out.println("Your input can not include quotation mark");
            return false;
        }
        return specification.length() <= 200;
    }

    public double getPrice() throws SQLException {
        InputScanner priceScanner = new InputScanner();
        if (priceScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (priceScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if (!isValidPriceInput(priceScanner.getInput().trim())) {
            System.out.print("Invalid price! Please input again: ");
            return getPrice();
        }
        return Double.parseDouble(priceScanner.getInput().trim());
    }

    public static boolean isValidPriceInput(String priceInput) {
        try {
            double price = Double.parseDouble(priceInput);
            if (price <= 0) {
                return false;
            }
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }

    public int getAvailableQuantity() throws SQLException {
        InputScanner quantityScanner = new InputScanner();
        if (quantityScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (quantityScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if (!isValidQuantityInput(quantityScanner.getInput().trim())) {
            System.out.print("Invalid quantity input! Please input again: ");
            return getAvailableQuantity();
        }
        return Integer.parseInt(quantityScanner.getInput().trim());

    }

    public static boolean isValidQuantityInput(String quantityInput) {
        try {
            double quantity = Integer.parseInt(quantityInput);
            if (quantity < 0) {
                return false;
            }
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }

    public String getCategory() throws SQLException {
        InputScanner categoryScanner = new InputScanner();
        if (categoryScanner.getInput().equals("B")) {
            AdministratorPanelExecutor administratorPanelExecutor = new AdministratorPanelExecutor(this.phoneNumber);
            administratorPanelExecutor.executeInstruction();
        }
        if (categoryScanner.getInput().equals("Q")) {
            QuitExecutor quitExecutor = new QuitExecutor();
            quitExecutor.executeInstruction();
        }
        if (!isValidCategory(categoryScanner.getInput().trim())) {
            System.out.print("Invalid category! Please input again: ");
            return getCategory();
        }
        return categoryScanner.getInput().trim();
    }

    public static boolean isValidCategory(String category) {
        if (!noQuotationMark(category)) {
            System.out.println("Your input can not include quotation mark");
            return false;
        }
        return category.length() <= 20;
    }

}
