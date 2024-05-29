package instructionTest.SystemTest;

import database.Database;
import model.InputScanner;

import model.instruction.system.LoginChoiceExecutor;
import model.instruction.system.LoginCustomerExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LoginCustomerExecutorTest {

    private InputScanner mockPhoneNumberScanner;
    private InputScanner mockPasswordScanner;
    private InputScanner mockChoiceScanner;
    private Database mockDatabase;
    private ResultSet mockResultSet;
    private LoginChoiceExecutor mockLoginChoiceExecutor;
    private LoginCustomerExecutor loginCustomerExecutor;

    @Before
    public void setUp() {
        mockPhoneNumberScanner = Mockito.mock(InputScanner.class);
        mockPasswordScanner = Mockito.mock(InputScanner.class);
        mockChoiceScanner = Mockito.mock(InputScanner.class);
        mockDatabase = Mockito.mock(Database.class);
        mockResultSet = Mockito.mock(ResultSet.class);
        mockLoginChoiceExecutor = Mockito.mock(LoginChoiceExecutor.class);
        loginCustomerExecutor = new LoginCustomerExecutor();
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

        loginCustomerExecutor.executeInstruction();

        verifyZeroInteractions(mockLoginChoiceExecutor);
        assertTrue(getLoggedInFieldValue(loginCustomerExecutor));
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

        loginCustomerExecutor.executeInstruction();

        verifyZeroInteractions(mockLoginChoiceExecutor);
        assertFalse(getLoggedInFieldValue(loginCustomerExecutor));
    }

    @Test
    public void testExecuteInstruction_PhoneNumberNotFound() throws SQLException {
        String phoneNumber = "123456789";
        String password = "password";
        when(mockPhoneNumberScanner.getInput()).thenReturn(phoneNumber);
        when(mockPasswordScanner.getInput()).thenReturn(password);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        loginCustomerExecutor.executeInstruction();

        verifyZeroInteractions(mockLoginChoiceExecutor);
        assertFalse(getLoggedInFieldValue(loginCustomerExecutor));
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

        loginCustomerExecutor.executeInstruction();

        verifyZeroInteractions(mockLoginChoiceExecutor);
        assertFalse(getLoggedInFieldValue(loginCustomerExecutor));
    }

    private boolean getLoggedInFieldValue(LoginCustomerExecutor executor) {
        try {
            java.lang.reflect.Field loggedInField = LoginCustomerExecutor.class.getDeclaredField("loggedIn");
            loggedInField.setAccessible(true);
            return (boolean) loggedInField.get(executor);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
