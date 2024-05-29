package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerTopUpExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerTopUpExecutorTest {

    @InjectMocks
    private CustomerTopUpExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() {
        System.setOut(null);

        executor.information();
    }

    @Test
    public void testGetBalance() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        when(mockDatabase.query(anyString())).thenReturn(mock(ResultSet.class));
        when(mockInputScanner.getInput()).thenReturn("50.0");

        double balance = executor.getBalance();

        assertEquals(0, balance, 0);
    }

    @Test
    public void testIsValidAmount() {
        assertTrue(CustomerTopUpExecutor.isValidAmount("50.0"));
        assertFalse(CustomerTopUpExecutor.isValidAmount("invalid"));
    }
}
