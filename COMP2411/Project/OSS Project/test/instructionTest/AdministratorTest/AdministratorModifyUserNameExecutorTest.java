package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorModifyUserNameExecutor;
import model.instruction.administrator.AdministratorPanelExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AdministratorModifyUserNameExecutorTest {

    private AdministratorModifyUserNameExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorModifyUserNameExecutor("1234567890");
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        String expectedOutput = "*********************************************************************************\n" +
                "You are now editing your personal information\n\n" +
                "    [B] Back\n" +
                "    [Q] Quit\n\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    @Test
    public void testChoose() throws SQLException {
        executor.choose("input");
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();
    }

    @Test
    public void testGetUsername_validInput() throws SQLException {
        setInputScanner("newUsername");

        String username = executor.getUsername();
        assertEquals("newUsername", username);
    }

    @Test
    public void testGetUsername_invalidInputThenValidInput() throws SQLException {
        setInputScanner("invalidUsername\nnewUsername");

        String username = executor.getUsername();
        assertEquals("newUsername", username);
    }

    @Test
    public void testGetUsername_backToMenu() throws SQLException {
        setInputScanner("B");

        AdministratorPanelExecutor mockPanelExecutor = mock(AdministratorPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();

        executor.getUsername();

        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testGetUsername_quitProgram() throws SQLException {
        setInputScanner("Q");

        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);
        doNothing().when(mockQuitExecutor).executeInstruction();

        executor.getUsername();

        verify(mockQuitExecutor, times(1)).executeInstruction();
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}


