package instructionTest.SystemTest;

import model.InputScanner;
import model.OSS;

import model.instruction.system.LoginAdministratorExecutor;
import model.instruction.system.LoginChoiceExecutor;
import model.instruction.system.LoginCustomerExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

public class LoginChoiceExecutorTest {

    private InputScanner mockScanner;
    private LoginAdministratorExecutor mockLoginAdminExecutor;
    private LoginCustomerExecutor mockLoginCustomerExecutor;
    private OSS mockOSS;
    private QuitExecutor mockQuitExecutor;
    private LoginChoiceExecutor loginChoiceExecutor;

    @Before
    public void setUp() {
        mockScanner = mock(InputScanner.class);
        mockLoginAdminExecutor = mock(LoginAdministratorExecutor.class);
        mockLoginCustomerExecutor = mock(LoginCustomerExecutor.class);
        mockOSS = mock(OSS.class);
        mockQuitExecutor = mock(QuitExecutor.class);
        loginChoiceExecutor = new LoginChoiceExecutor();
    }

    @Test
    public void testChooseAdministrator() throws SQLException {
        when(mockScanner.getInput()).thenReturn("A");
        loginChoiceExecutor.choose(mockScanner.getInput());
        verify(mockLoginAdminExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginCustomerExecutor, mockOSS, mockQuitExecutor);
    }

    @Test
    public void testChooseCustomer() throws SQLException {
        when(mockScanner.getInput()).thenReturn("C");
        loginChoiceExecutor.choose(mockScanner.getInput());
        verify(mockLoginCustomerExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginAdminExecutor, mockOSS, mockQuitExecutor);
    }

    @Test
    public void testChooseBack() throws SQLException {
        when(mockScanner.getInput()).thenReturn("B");
        loginChoiceExecutor.choose(mockScanner.getInput());
        verify(mockOSS, times(1)).activate();
        verifyZeroInteractions(mockLoginAdminExecutor, mockLoginCustomerExecutor, mockQuitExecutor);
    }

    @Test
    public void testChooseQuit() throws SQLException {
        when(mockScanner.getInput()).thenReturn("Q");
        loginChoiceExecutor.choose(mockScanner.getInput());
        verify(mockQuitExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginAdminExecutor, mockLoginCustomerExecutor, mockOSS);
    }

    @Test
    public void testChooseInvalidInput() throws SQLException {
        String invalidInput = "Invalid";
        when(mockScanner.getInput()).thenReturn(invalidInput);
        loginChoiceExecutor.choose(mockScanner.getInput());
        verify(mockScanner, times(2)).getInput(); // Once in choose() and once in the default case
        verifyZeroInteractions(mockLoginAdminExecutor, mockLoginCustomerExecutor, mockOSS, mockQuitExecutor);
    }
}