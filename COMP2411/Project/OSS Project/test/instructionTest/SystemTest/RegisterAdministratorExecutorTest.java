package instructionTest.SystemTest;

import database.Database;
import model.InputScanner;
import model.OSS;
import model.instruction.system.RegisterAdministratorExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RegisterAdministratorExecutorTest {

    @Mock
    private Database mockDatabase;
    @Mock
    private InputScanner mockInputScanner;
    @Mock
    private OSS mockOSS;

    private RegisterAdministratorExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        executor = new RegisterAdministratorExecutor();
    }


    @Test
    public void testInformation() {
        // Test the information method, no assertions needed for console output
        executor.information();
    }

    @Test
    public void testGetPhoneNumber_validNumber_returnsPhoneNumber() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("1234567890");

        String phoneNumber = executor.getPhoneNumber();

        assertEquals("1234567890", phoneNumber);
    }

    @Test
    public void testGetPhoneNumber_backCommand_activatesOSS() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getPhoneNumber();

        // Verify that the OSS activation is performed
        verify(mockOSS).activate();
    }



    @Test
    public void testGetPhoneNumber_invalidNumber_promptsForInputAgain() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("1234567890"); // Valid number

        String phoneNumber = executor.getPhoneNumber();

        assertEquals("1234567890", phoneNumber);
    }

    @Test
    public void testIsValidPhoneNumber_validNumber_returnsTrue() throws SQLException {
        assertTrue(RegisterAdministratorExecutor.isValidPhoneNumber("1234567890"));
    }

    @Test
    public void testIsValidPhoneNumber_invalidNumber_returnsFalse() throws SQLException {
        assertFalse(RegisterAdministratorExecutor.isValidPhoneNumber("12345abcde"));
    }


    @Test
    public void testGetUsername_validUsername_returnsUsername() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("john_doe");

        String username = executor.getUsername();

        assertEquals("john_doe", username);
    }

    @Test
    public void testGetUsername_backCommand_activatesOSS() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getUsername();

        // Verify that the OSS activation is performed
        verify(mockOSS).activate();
    }


    @Test
    public void testGetUsername_invalidUsername_promptsForInputAgain() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("john_doe"); // Valid username

        String username = executor.getUsername();

        assertEquals("john_doe", username);
    }

    @Test
    public void testIsValidUsername_validUsername_returnsTrue() {
        assertTrue(RegisterAdministratorExecutor.isValidUsername("john_doe"));
    }

    @Test
    public void testIsValidUsername_invalidUsername_returnsFalse() {
        assertFalse(RegisterAdministratorExecutor.isValidUsername("john!doe"));
    }

    @Test
    public void testGetPassword_validPassword_returnsPassword() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("password123");

        String password = executor.getPassword();

        assertEquals("password123", password);
    }

    @Test
    public void testGetPassword_backCommand_activatesOSS() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getPassword();

        // Verify that the OSS activation is performed
        verify(mockOSS).activate();
    }


    @Test
    public void testGetPassword_invalidPassword_promptsForInputAgain() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("password123"); // Valid password

        String password = executor.getPassword();

        assertEquals("password123", password);
    }

    @Test
    public void testIsValidPassword_validPassword_returnsTrue() {
        assertTrue(RegisterAdministratorExecutor.isValidPassword("password123"));
    }

    @Test
    public void testIsValidPassword_invalidPassword_returnsFalse() {
        assertFalse(RegisterAdministratorExecutor.isValidPassword("password123456789012345"));
    }

}