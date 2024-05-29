package instructionTest.AdministratorTest;

import database.Database;
import model.InputScanner;
import model.instruction.administrator.AdministratorFilterSpecificationExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AdministratorFilterSpecificationExecutorTest {

    private AdministratorFilterSpecificationExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new AdministratorFilterSpecificationExecutor("123456");
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("Test Specification")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("Test Specification\nQ".getBytes()));

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
    public void testGetKeyword() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("Test Specification")
                .thenReturn("invalid")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("Test Specification\ninvalid\nQ".getBytes()));

        assertEquals("Test Specification", executor.getKeyword());
        assertEquals("Test Specification", executor.getKeyword());
        assertTrue(executor.getKeyword().isEmpty());
    }
}

