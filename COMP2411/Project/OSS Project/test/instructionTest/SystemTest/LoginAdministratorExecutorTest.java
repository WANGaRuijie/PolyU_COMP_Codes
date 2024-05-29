package instructionTest.SystemTest;

import database.Database;
import model.InputScanner;

import model.instruction.administrator.AdministratorPanelExecutor;
import model.instruction.system.LoginAdministratorExecutor;
import model.instruction.system.LoginChoiceExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginAdministratorExecutorTest {

    private InputScanner mockPhoneNumberScanner;
    private InputScanner mockPasswordScanner;
    private InputScanner mockChoiceScanner;
    private Database mockDatabase;
    private ResultSet mockResultSet;
    private LoginChoiceExecutor mockLoginChoiceExecutor;
    private AdministratorPanelExecutor mockAdminPanelExecutor;
    private LoginAdministratorExecutor loginAdministratorExecutor;

    @Before
    public void setUp() {
        mockPhoneNumberScanner = Mockito.mock(InputScanner.class);
        mockPasswordScanner = Mockito.mock(InputScanner.class);
        mockChoiceScanner = Mockito.mock(InputScanner.class);
        mockDatabase = Mockito.mock(Database.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockLoginChoiceExecutor = Mockito.mock(LoginChoiceExecutor.class);
        mockAdminPanelExecutor = Mockito.mock(AdministratorPanelExecutor.class);
        loginAdministratorExecutor = new LoginAdministratorExecutor();
    }

    @Test
    public void testExecuteInstruction_SuccessfulLogin() throws SQLException {
        String phoneNumber = "123456789";
        String password = "password";
        when(mockPhoneNumberScanner.getInput()).thenReturn(phoneNumber);
        when(mockPasswordScanner.getInput()).thenReturn(password);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("PASSWORD")).thenReturn(password);

        loginAdministratorExecutor.executeInstruction();

        verify(mockAdminPanelExecutor, times(1)).executeInstruction();
        verifyZeroInteractions(mockLoginChoiceExecutor);
        assertTrue(getLoggedInFieldValue(loginAdministratorExecutor));
    }

    @Test
    public void testExecuteInstruction_IncorrectPassword() throws SQLException {
        String phoneNumber = "123456789";
        String password = "password";
        String incorrectPassword = "wrongpassword";
        when(mockPhoneNumberScanner.getInput()).thenReturn(phoneNumber);
        when(mockPasswordScanner.getInput()).thenReturn(incorrectPassword);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("PASSWORD")).thenReturn(password);

        loginAdministratorExecutor.executeInstruction();

        verifyZeroInteractions(mockAdminPanelExecutor, mockLoginChoiceExecutor);
        assertFalse(getLoggedInFieldValue(loginAdministratorExecutor));
    }

    @Test
    public void testExecuteInstruction_PhoneNumberNotFound() throws SQLException {
        String phoneNumber = "123456789";
        String password = "password";
        when(mockPhoneNumberScanner.getInput()).thenReturn(phoneNumber);
        when(mockPasswordScanner.getInput()).thenReturn(password);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        loginAdministratorExecutor.executeInstruction();

        verifyZeroInteractions(mockAdminPanelExecutor, mockLoginChoiceExecutor);
        assertFalse(getLoggedInFieldValue(loginAdministratorExecutor));
    }

    @Test
    public void testExecuteInstruction_BackToLoginChoice() throws SQLException {
        String phoneNumber = "123456789";
        String password = "password";
        String choice = "B";
        when(mockPhoneNumberScanner.getInput()).thenReturn(phoneNumber);
        when(mockPasswordScanner.getInput()).thenReturn(password);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("PASSWORD")).thenReturn(password);
        when(mockChoiceScanner.getInput()).thenReturn(choice);

        loginAdministratorExecutor.executeInstruction();

        verifyZeroInteractions(mockAdminPanelExecutor);
        verify(mockLoginChoiceExecutor, times(1)).executeInstruction();
        assertFalse(getLoggedInFieldValue(loginAdministratorExecutor));
    }

    private boolean getLoggedInFieldValue(LoginAdministratorExecutor executor) {
        try {
            java.lang.reflect.Field loggedInField = LoginAdministratorExecutor.class.getDeclaredField("loggedIn");
            loggedInField.setAccessible(true);
            return (boolean) loggedInField.get(executor);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
