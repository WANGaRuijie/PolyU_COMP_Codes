package instructionTest.AdministratorTest;

import database.Database;
import model.InputScanner;
import model.instruction.administrator.AdministratorFilterPriceExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AdministratorFilterPriceExecutorTest {

    private AdministratorFilterPriceExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new AdministratorFilterPriceExecutor("123456");
        executor.numberA = 10.0;
        executor.numberB = 20.0;
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("15.0")
                .thenReturn("25.0")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("15.0\n25.0\nQ".getBytes()));

        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        executor.executeInstruction();

        verify(executor, times(1)).visitDatabase();
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("testInput");
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        executor.visitDatabase();

        verify(mockDatabase, times(1)).query(anyString());
    }

    @Test
    public void testGetNumberA() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("15.0")
                .thenReturn("invalid")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("15.0\ninvalid\nQ".getBytes()));

        assertEquals("15.0", executor.getNumberA());
        assertEquals("15.0", executor.getNumberA());
        assertTrue(executor.getNumberA().isEmpty());
    }

    @Test
    public void testGetNumberB() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("25.0")
                .thenReturn("invalid")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("25.0\ninvalid\nQ".getBytes()));

        assertEquals("25.0", executor.getNumberB());
        assertEquals("25.0", executor.getNumberB());
        assertTrue(executor.getNumberB().isEmpty());
    }
}


