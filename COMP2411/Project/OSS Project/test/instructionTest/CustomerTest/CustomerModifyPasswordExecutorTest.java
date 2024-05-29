package instructionTest.CustomerTest;

import model.instruction.customer.CustomerModifyPasswordExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class CustomerModifyPasswordExecutorTest {

    private CustomerModifyPasswordExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new CustomerModifyPasswordExecutor("123"); // Use a valid phone number for testing
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
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();
        assertEquals("Some expected output", outContent.toString().trim());
    }

    @Test
    public void testGetPassword() throws SQLException {
        setInputScanner("ValidPassword");
        String password = executor.getPassword();
        assertEquals("ValidPassword", password);
    }

    @Test
    public void testGetPersonalPassword() throws SQLException {
        ResultSet resultSet = executor.getPersonalPassword("123");
        // Add assertions based on your implementation
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

