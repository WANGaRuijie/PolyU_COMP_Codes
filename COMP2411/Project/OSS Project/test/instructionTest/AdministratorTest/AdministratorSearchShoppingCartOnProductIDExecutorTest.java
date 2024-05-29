package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchShoppingCartOnProductIDExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class AdministratorSearchShoppingCartOnProductIDExecutorTest {

    private AdministratorSearchShoppingCartOnProductIDExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorSearchShoppingCartOnProductIDExecutor("123"); // Use a valid phone number for testing
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        assertEquals("", outContent.toString());
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("input");
        assertEquals("", outContent.toString());
    }

    @Test
    public void testGetProductID() throws SQLException {
        setInputScanner("ProductID");
        String productID = executor.getProductID();
        assertEquals("ProductID", productID);
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

