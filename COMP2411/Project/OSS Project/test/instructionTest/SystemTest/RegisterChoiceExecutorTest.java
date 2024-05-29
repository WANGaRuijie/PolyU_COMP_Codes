package instructionTest.SystemTest;

import model.InputScanner;
import model.OSS;
import model.instruction.system.QuitExecutor;
import model.instruction.system.RegisterAdministratorExecutor;
import model.instruction.system.RegisterChoiceExecutor;
import model.instruction.system.RegisterCustomerExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class RegisterChoiceExecutorTest {

    @Mock
    private InputScanner mockInputScanner;

    private RegisterChoiceExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        executor = new RegisterChoiceExecutor();
    }

    @Test
    public void testInformation() {
        // Test the information method, no assertions needed for console output
        executor.information();
    }

    @Test
    public void testExecuteInstruction_successfullyRegistersAdministrator() throws SQLException {
        RegisterAdministratorExecutor mockRegisterAdministratorExecutor = mock(RegisterAdministratorExecutor.class);
        executor.choose("A");

        // Verify that the RegisterAdministratorExecutor is instantiated and executeInstruction is called
        verify(mockRegisterAdministratorExecutor).executeInstruction();
    }

    @Test
    public void testExecuteInstruction_successfullyRegistersCustomer() throws SQLException {
        RegisterCustomerExecutor mockRegisterCustomerExecutor = mock(RegisterCustomerExecutor.class);
        executor.choose("C");

        // Verify that the RegisterCustomerExecutor is instantiated and executeInstruction is called
        verify(mockRegisterCustomerExecutor).executeInstruction();
    }

    @Test
    public void testExecuteInstruction_activatesOSS() throws SQLException {
        OSS mockOSS = mock(OSS.class);
        executor.choose("B");

        // Verify that the OSS is instantiated and activate is called
        verify(mockOSS).activate();
    }

    @Test
    public void testExecuteInstruction_executesQuitInstruction() throws SQLException {
        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);
        executor.choose("Q");

        // Verify that the QuitExecutor is instantiated and executeInstruction is called
        verify(mockQuitExecutor).executeInstruction();
    }

    @Test
    public void testExecuteInstruction_invalidInput_promptsForInputAgain() throws SQLException {
        InputScanner mockInputScanner = mock(InputScanner.class);
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("A"); // Valid input
        executor.choose("X");

        // Verify that the choose method is called recursively with a valid input
        verify(mockInputScanner, times(2)).getInput();
        verify(mockInputScanner).getInput();
        verify(mockInputScanner).getInput();
    }

    @Test
    public void testChoose_invalidInput_promptsForInputAgain() throws SQLException {
        System.out.print("Invalid input! Please input again: ");
        InputScanner mockInputScanner = mock(InputScanner.class);
        when(mockInputScanner.getInput())
                .thenReturn("invalid")
                .thenReturn("A"); // Valid input
        executor.choose("X");

        // Verify that the choose method is called recursively with a valid input
        verify(mockInputScanner, times(2)).getInput();
        verify(mockInputScanner).getInput();
        verify(mockInputScanner).getInput();
    }

    @Test
    public void testVisitDatabase_doesNothing() throws SQLException {
        executor.visitDatabase(); // Should not throw an exception
    }
}
