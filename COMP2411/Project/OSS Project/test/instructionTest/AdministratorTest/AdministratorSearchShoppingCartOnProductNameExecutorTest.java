package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchShoppingCartOnProductNameExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class AdministratorSearchShoppingCartOnProductNameExecutorTest {

    private AdministratorSearchShoppingCartOnProductNameExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorSearchShoppingCartOnProductNameExecutor("123"); // Use a valid phone number for testing
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
    public void testGetProductName() throws SQLException {
        setInputScanner("ProductName");
        String productName = executor.getProductName();
        assertEquals("ProductName", productName);
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

