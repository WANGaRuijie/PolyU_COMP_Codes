package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorAddProductExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.Assert.*;
public class AdministratorAddProductExecutorTest {

    private AdministratorAddProductExecutor executor;

    @Before
    public void setUp() {
        executor = new AdministratorAddProductExecutor("1234567890");
    }

    @Test
    public void testIsValidProductID_ValidID_ReturnsTrue() throws SQLException {
        assertTrue(AdministratorAddProductExecutor.isValidProductID("12345"));
    }

    @Test
    public void testIsValidProductID_IDTooLong_ReturnsFalse() throws SQLException {
        assertFalse(AdministratorAddProductExecutor.isValidProductID("123456789012345678901"));
    }

    @Test
    public void testIsValidProductID_ContainsNonDigit_ReturnsFalse() throws SQLException {
        assertFalse(AdministratorAddProductExecutor.isValidProductID("1234a"));
    }


    @Test
    public void testIsValidProductName_ValidName_ReturnsTrue() throws SQLException {
        assertTrue(AdministratorAddProductExecutor.isValidProductName("Product_123"));
    }

    @Test
    public void testIsValidProductName_NameTooLong_ReturnsFalse() throws SQLException {
        assertFalse(AdministratorAddProductExecutor.isValidProductName("Product_12345678901234567890"));
    }

    @Test
    public void testIsValidProductName_ContainsInvalidCharacters_ReturnsFalse() throws SQLException {
        assertFalse(AdministratorAddProductExecutor.isValidProductName("Product*123"));
    }

    @Test
    public void testIsValidDescription_ValidDescription_ReturnsTrue() {
        assertTrue(AdministratorAddProductExecutor.isValidDescription("This is a description."));
    }

    @Test
    public void testIsValidDescription_DescriptionTooLong_ReturnsFalse() {
        assertFalse(AdministratorAddProductExecutor.isValidDescription("This is a very long description that exceeds the maximum character limit. " +
                "This is a very long description that exceeds the maximum character limit. This is a very long description that exceeds the maximum character limit. " +
                "This is a very long description that exceeds the maximum character limit. This is a very long description that exceeds the maximum character limit."));
    }

    @Test
    public void testIsValidSpecification_ValidSpecification_ReturnsTrue() {
        assertTrue(AdministratorAddProductExecutor.isValidSpecification("This is a specification."));
    }

    @Test
    public void testIsValidSpecification_SpecificationTooLong_ReturnsFalse() {
        assertFalse(AdministratorAddProductExecutor.isValidSpecification("This is a very long specification that exceeds the maximum character limit. " +
                "This is a very long specification that exceeds the maximum character limit. This is a very long specification that exceeds the maximum character limit. " +
                "This is a very long specification that exceeds the maximum character limit. This is a very long specification that exceeds the maximum character limit."));
    }

    @Test
    public void testIsValidPriceInput_ValidInput_ReturnsTrue() {
        assertTrue(AdministratorAddProductExecutor.isValidPriceInput("9.99"));
    }

    @Test
    public void testIsValidPriceInput_InvalidInput_ReturnsFalse() {
        assertFalse(AdministratorAddProductExecutor.isValidPriceInput("abc"));
    }

    @Test
    public void testIsValidQuantityInput_ValidInput_ReturnsTrue() {
        assertTrue(AdministratorAddProductExecutor.isValidQuantityInput("10"));
    }

    @Test
    public void testIsValidQuantityInput_InvalidInput_ReturnsFalse() {
        assertFalse(AdministratorAddProductExecutor.isValidQuantityInput("abc"));
    }


    @Test
    public void testIsValidCategory_CategoryTooLong_ReturnsFalse() {
        assertFalse(AdministratorAddProductExecutor.isValidCategory("ThisIsAVeryLongCategoryNameThatExceedsTheLimit"));
    }

    @Test
    public void testGetProductID_ValidInput_ReturnsProductID() throws SQLException {
        String input = "12345\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        String productID = executor.getProductID();

        assertEquals("12345", productID);
    }

    @Test
    public void testIsValidProductName_LongName_ReturnsFalse() throws SQLException {
        boolean isValid = AdministratorAddProductExecutor.isValidProductName("ThisIsALongProductName123");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidProductName_InvalidCharacters_ReturnsFalse() throws SQLException {
        boolean isValid = AdministratorAddProductExecutor.isValidProductName("Product@#");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidDescription_LongDescription_ReturnsFalse() {
        boolean isValid = AdministratorAddProductExecutor.isValidDescription("This is a very long product description that exceeds the maximum limit of characters allowed.");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidSpecification_LongSpecification_ReturnsFalse() {
        boolean isValid = AdministratorAddProductExecutor.isValidSpecification("This is a very long product specification that exceeds the maximum limit of characters allowed.");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidPriceInput_ValidPrice_ReturnsTrue() {
        boolean isValid = AdministratorAddProductExecutor.isValidPriceInput("9.99");
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidPriceInput_NegativePrice_ReturnsFalse() {
        boolean isValid = AdministratorAddProductExecutor.isValidPriceInput("-9.99");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidQuantityInput_ValidQuantity_ReturnsTrue() {
        boolean isValid = AdministratorAddProductExecutor.isValidQuantityInput("10");
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidQuantityInput_NegativeQuantity_ReturnsFalse() {
        boolean isValid = AdministratorAddProductExecutor.isValidQuantityInput("-10");
        Assert.assertFalse(isValid);
    }

    @Test
    public void testIsValidCategory_ValidCategory_ReturnsTrue() {
        boolean isValid = AdministratorAddProductExecutor.isValidCategory("Electronics");
        Assert.assertTrue(isValid);
    }

    @Test
    public void testIsValidCategory_LongCategory_ReturnsFalse() {
        boolean isValid = AdministratorAddProductExecutor.isValidCategory("ThisIsALongCategoryName123");
        Assert.assertFalse(isValid);
    }

}

