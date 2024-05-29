package instructionTest.AdministratorTest;

import model.InputScanner;
import model.instruction.administrator.AdministratorFilterExecutor;
import model.instruction.administrator.AdministratorFilterNameExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AdministratorFilterNameExecutorTest {

    private AdministratorFilterNameExecutor executor;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        executor = new AdministratorFilterNameExecutor("123");
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        assertNotEquals(expectedInformationMessage(), outContent.toString().trim());
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        String input = "TestKeyword\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        AdministratorFilterExecutor mockedExecutor = mock(AdministratorFilterExecutor.class);
        executor.choose(String.valueOf(new InputScanner()));
        verify(mockedExecutor).executeInstruction();
    }

    @Test
    public void testChooseWithBackInput() throws SQLException {
        String input = "B\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        AdministratorFilterExecutor mockedExecutor = mock(AdministratorFilterExecutor.class);
        executor.choose(String.valueOf(new InputScanner()));
        verify(mockedExecutor).executeInstruction();
    }

    @Test
    public void testChooseWithQuitInput() throws SQLException {
        String input = "Q\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        QuitExecutor mockedExecutor = mock(QuitExecutor.class);
        executor.choose(String.valueOf(new InputScanner()));
        verify(mockedExecutor).executeInstruction();
    }

    private String expectedInformationMessage() {
        return "*********************************************************************************\r\n" +
                "You are now filtering with a keyword on the product name\n" +
                "\r\n" +
                "    [B] Back\r\n" +
                "    [Q] Quit\r\n" +
                "\r\n" +
                "Please enter your keyword (no more than 20 characters):";
    }
}
