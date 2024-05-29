package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerFilterPriceExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerFilterPriceExecutorTest {

    @InjectMocks
    private CustomerFilterPriceExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private QuitExecutor mockQuitExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("B");
        verify(mockQuitExecutor, times(1)).executeInstruction();

        executor.choose("Q");
        verify(mockQuitExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        executor.visitDatabase();

        verify(mockDatabase, times(1)).query(anyString());
    }

    @Test
    public void testGetNumberA() throws SQLException {
        System.setIn(new ByteArrayInputStream("15".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("15");

        String numberA = executor.getNumberA();

        assertEquals("15", numberA);
    }

    @Test
    public void testGetNumberB() throws SQLException {
        System.setIn(new ByteArrayInputStream("25".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("25");

        String numberB = executor.getNumberB();

        assertEquals("25", numberB);
    }
}

