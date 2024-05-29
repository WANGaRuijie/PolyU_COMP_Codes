package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorModifyPhoneNumberExecutor;
import model.instruction.administrator.AdministratorPanelExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AdministratorModifyPhoneNumberExecutorTest {

    private AdministratorModifyPhoneNumberExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorModifyPhoneNumberExecutor("1234567890");
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
    public void testGetPhoneNumber_validInput() throws SQLException {
        setInputScanner("newPhoneNumber");

        String phoneNumber = executor.getPhoneNumber();
        assertEquals("newPhoneNumber", phoneNumber);
    }

    @Test
    public void testGetPhoneNumber_invalidInputThenValidInput() throws SQLException {
        setInputScanner("invalidPhoneNumber\nnewPhoneNumber");

        String phoneNumber = executor.getPhoneNumber();
        assertEquals("newPhoneNumber", phoneNumber);
    }

    @Test
    public void testGetPhoneNumber_backToMenu() throws SQLException {
        setInputScanner("B");

        AdministratorPanelExecutor mockPanelExecutor = mock(AdministratorPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();

        executor.getPhoneNumber();

        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testGetPhoneNumber_quitProgram() throws SQLException {
        setInputScanner("Q");

        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);
        doNothing().when(mockQuitExecutor).executeInstruction();

        executor.getPhoneNumber();

        verify(mockQuitExecutor, times(1)).executeInstruction();
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

