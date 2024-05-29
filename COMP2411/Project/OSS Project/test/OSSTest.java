import model.InputScanner;
import model.OSS;
import model.instruction.system.LoginChoiceExecutor;

import model.instruction.system.QuitExecutor;
import model.instruction.system.RegisterChoiceExecutor;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OSSTest {

    private InputScanner mockScanner;
    private LoginChoiceExecutor mockLoginChoiceExecutor;
    private RegisterChoiceExecutor mockRegisterChoiceExecutor;
    private QuitExecutor mockQuitExecutor;
    private OSS oss;

    @Before
    public void setUp() {
        mockScanner = mock(InputScanner.class);
        mockLoginChoiceExecutor = mock(LoginChoiceExecutor.class);
        mockRegisterChoiceExecutor = mock(RegisterChoiceExecutor.class);
        mockQuitExecutor = mock(QuitExecutor.class);
        oss = new OSS();
    }

    @Test
    public void  testChooseLogin() throws SQLException {
        when(mockScanner.getInput()).thenReturn("L");
        oss.choose(mockScanner.getInput());
        verify(mockLoginChoiceExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockRegisterChoiceExecutor, mockQuitExecutor);
    }

    @Test
    public void testChooseRegister() throws SQLException {
        when(mockScanner.getInput()).thenReturn("R");
        oss.choose(mockScanner.getInput());
        verify(mockRegisterChoiceExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginChoiceExecutor, mockQuitExecutor);
    }

    @Test
    public void testChooseQuit() throws SQLException {
        when(mockScanner.getInput()).thenReturn("Q");
        oss.choose(mockScanner.getInput());
        verify(mockQuitExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginChoiceExecutor, mockRegisterChoiceExecutor);
    }

    @Test
    public void testChooseInvalidInput() throws SQLException {
        String invalidInput = "Invalid";
        when(mockScanner.getInput()).thenReturn(invalidInput);
        oss.choose(mockScanner.getInput());
        verify(mockScanner, times(2)).getInput(); // Once in choose() and once in the default case
        verifyZeroInteractions(mockLoginChoiceExecutor, mockRegisterChoiceExecutor, mockQuitExecutor);
    }

    @Test
    public void testActivate() throws SQLException {
        OSS oss = new OSS();
        oss.activate();
    }
}
