package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorFilterDescriptionExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AdministratorFilterDescriptionExecutorTest {

    private AdministratorFilterDescriptionExecutor executor;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        executor = new AdministratorFilterDescriptionExecutor("123");
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        assertNotEquals(expectedInformationMessage(), outContent.toString().trim());
    }


    @Test
    public void testGetKeyword() throws SQLException {
        String input = "TestKeyword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        String keyword = executor.getKeyword();
        assertEquals("TestKeyword", keyword);
    }

    private String expectedInformationMessage() {
        return "*********************************************************************************\n" +
                "You are now filtering with a keyword on the product description\n" +
                "\n" +
                "    [B] Back\n" +
                "    [Q] Quit\n" +
                "\n" +
                "Please enter your keyword (no more than 200 characters): ";
    }
}
