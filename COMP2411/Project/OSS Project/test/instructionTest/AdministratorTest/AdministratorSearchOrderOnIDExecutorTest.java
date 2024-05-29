package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchOrderOnIDExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdministratorSearchOrderOnIDExecutorTest {

    private AdministratorSearchOrderOnIDExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorSearchOrderOnIDExecutor("123"); // Use a valid phone number for testing
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
    public void testGetOrderID() throws SQLException {
        setInputScanner("1234567890");
        String orderID = executor.getOrderID();
        assertEquals("1234567890", orderID);
    }

    @Test
    public void testIsValidOrderID() throws SQLException {
        boolean isValid = executor.isValidOrderID("1234567890");
        assertTrue(isValid);
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

