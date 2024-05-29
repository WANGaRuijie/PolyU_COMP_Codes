package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchCustomerOnPhoneNumberExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AdministratorSearchCustomerOnPhoneNumberExecutorTest {

    private AdministratorSearchCustomerOnPhoneNumberExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorSearchCustomerOnPhoneNumberExecutor("123"); // Use a valid phone number for testing
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
    public void testGetCustomerPhoneNumber() throws SQLException {
        setInputScanner("1234567890");
        String customerPhoneNumber = executor.getCustomerPhoneNumber();
        assertEquals("1234567890", customerPhoneNumber);
    }

    @Test
    public void testIsValidCustomerPhoneNumber() throws SQLException {
        boolean isValid = executor.isValidCustomerPhoneNumber("1234567890");
        assertTrue(isValid);
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

