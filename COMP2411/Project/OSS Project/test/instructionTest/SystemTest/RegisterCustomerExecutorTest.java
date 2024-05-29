package instructionTest.SystemTest;

import database.Database;
import model.InputScanner;
import model.InstructionExecutor;
import model.OSS;
import model.instruction.system.QuitExecutor;
import model.instruction.system.RegisterCustomerExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RegisterCustomerExecutorTest {

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private Database mockDatabase;

    private RegisterCustomerExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        executor = new RegisterCustomerExecutor();
    }

    @Test
    public void testInformation() {
        executor.information();
    }

    @Test
    public void testExecuteInstruction_registersCustomerAndActivatesOSS() throws SQLException {
        OSS mockOSS = mock(OSS.class);

        when(mockInputScanner.getInput())
                .thenReturn("1234567890")
                .thenReturn("test_username")
                .thenReturn("password")
                .thenReturn("B");

        when(mockDatabase.contains("CUSTOMER", "PHONE_NUMBER", "1234567890"))
                .thenReturn(false);

        executor.executeInstruction();

        verify(mockDatabase).insert("INSERT INTO CUSTOMER (PHONE_NUMBER, USER_NAME, PASSWORD) VALUES ('1234567890', 'test_username', 'password')");

        verify(mockOSS).activate();
    }

    @Test
    public void testExecuteInstruction_invalidPhoneNumber_promptsForInputAgain() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("1234567890")
                .thenReturn("test_username")
                .thenReturn("password")
                .thenReturn("B");

        when(mockDatabase.contains("CUSTOMER", "PHONE_NUMBER", "1234567890"))
                .thenReturn(false);

        executor.executeInstruction();

        verify(mockInputScanner, times(5)).getInput();
    }

    @Test
    public void testExecuteInstruction_repeatedPhoneNumber_promptsForInputAgain() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("1234567890")
                .thenReturn("test_username")
                .thenReturn("password")
                .thenReturn("B");

        when(mockDatabase.contains("CUSTOMER", "PHONE_NUMBER", "1234567890"))
                .thenReturn(true);

        executor.executeInstruction();

        verify(mockInputScanner, times(5)).getInput();
    }

    @Test
    public void testChoose_doesNothing() {
        executor.choose("input");
    }

    @Test
    public void testGetPhoneNumber_validInput_returnsPhoneNumber() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("1234567890");

        String phoneNumber = executor.getPhoneNumber();

        verify(mockInputScanner).getInput();

        assertEquals("1234567890", phoneNumber);
    }

    @Test
    public void testGetPhoneNumber_backOption_activatesOSS() throws SQLException {
        OSS mockOSS = mock(OSS.class);

        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getPhoneNumber();

        verify(mockOSS).activate();
    }

    @Test
    public void testGetPhoneNumber_quitOption_executesQuitInstruction() throws SQLException {
        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);

        when(mockInputScanner.getInput())
                .thenReturn("Q");

        executor.getPhoneNumber();

        verify(mockQuitExecutor).executeInstruction();
    }

    @Test
    public void testGetUsername_validInput_returnsUsername() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("test_username");

        String username = executor.getUsername();

        verify(mockInputScanner).getInput();

        assertEquals("test_username", username);
    }

    @Test
    public void testGetUsername_backOption_activatesOSS() throws SQLException {
        OSS mockOSS = mock(OSS.class);

        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getUsername();

        verify(mockOSS).activate();
    }

    @Test
    public void testGetUsername_quitOption_executesQuitInstruction() throws SQLException {
        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);

        when(mockInputScanner.getInput())
                .thenReturn("Q");

        executor.getUsername();

        verify(mockQuitExecutor).executeInstruction();
    }

    @Test
    public void testGetPassword_validInput_returnsPassword() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("password");

        String password = executor.getPassword();

        verify(mockInputScanner).getInput();

        assertEquals("password", password);
    }

    @Test
    public void testGetPassword_backOption_activatesOSS() throws SQLException {
        OSS mockOSS = mock(OSS.class);

        when(mockInputScanner.getInput())
                .thenReturn("B");

        executor.getPassword();

        verify(mockOSS).activate();
    }
    @Test
    public void testGetPassword_quitOption_executesQuitInstruction() throws SQLException {
        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);

        when(mockInputScanner.getInput())
                .thenReturn("Q");

        executor.getPassword();

        verify(mockQuitExecutor).executeInstruction();
    }

    @Test
    public void testIsValidPhoneNumber_validPhoneNumber_returnsTrue() throws SQLException {

        assertTrue(RegisterCustomerExecutor.isValidPhoneNumber("1234567890"));
    }

    @Test
    public void testIsValidPhoneNumber_invalidPhoneNumber_returnsFalse() throws SQLException {

        assertFalse(RegisterCustomerExecutor.isValidPhoneNumber("123456789012345678901"));

        assertFalse(RegisterCustomerExecutor.isValidPhoneNumber("12345abcde"));

        when(mockDatabase.contains("CUSTOMER", "PHONE_NUMBER", "1234567890"))
                .thenReturn(true);
        assertFalse(RegisterCustomerExecutor.isValidPhoneNumber("1234567890"));
    }

    @Test
    public void testIsValidUsername_validUsername_returnsTrue() {
        assertTrue(RegisterCustomerExecutor.isValidUsername("test_username"));
    }

    @Test
    public void testIsValidUsername_invalidUsername_returnsFalse() {
        assertFalse(RegisterCustomerExecutor.isValidUsername("test_username_test_username"));
    }
}