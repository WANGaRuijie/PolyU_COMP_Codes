package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorFilterExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AdministratorFilterExecutorTest {

    private AdministratorFilterExecutor executor;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        executor = new AdministratorFilterExecutor("123");
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        assertEquals(expectedInformationMessage(), outContent.toString().trim());
    }

    @Test
    public void testChooseWithQuitInput() throws SQLException {
        String input = "Q\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        QuitExecutor mockedExecutor = mock(QuitExecutor.class);
        executor.choose(" ");
        verify(mockedExecutor).executeInstruction();
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();  // Nothing to test in this method
    }

    private String expectedInformationMessage() {
        return "*********************************************************************************\r\n" +
                "You are now filtering the products\r\n" +
                "\r\n" +
                "    [F1] Filter with a key word in the product name\r\n" +
                "    [F2] Filter with a key word in the product description\r\n" +
                "    [F3] Filter with a key word in the product specification\r\n" +
                "    [F4] Filter with a criterion on the product price\r\n" +
                "    [F5] Filter with a key word in the product category\r\n" +
                "\r\n" +
                "    [B] Back\r\n" +
                "    [Q] Quit\r\n" +
                "\r\n" +
                "Please enter your choice:";
    }
}


