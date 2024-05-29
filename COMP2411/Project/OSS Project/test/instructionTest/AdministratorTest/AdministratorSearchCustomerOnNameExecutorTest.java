package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchCustomerOnNameExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class AdministratorSearchCustomerOnNameExecutorTest {

    private AdministratorSearchCustomerOnNameExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorSearchCustomerOnNameExecutor("123"); // Use a valid phone number for testing
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
    public void testGetCustomerName() throws SQLException {
        setInputScanner("John Doe");
        String customerName = executor.getCustomerName();
        assertEquals("John Doe", customerName);
    }


    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

