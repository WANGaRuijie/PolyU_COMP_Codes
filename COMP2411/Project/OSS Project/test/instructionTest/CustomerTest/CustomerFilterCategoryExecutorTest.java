package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerFilterCategoryExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerFilterCategoryExecutorTest {

    @InjectMocks
    private CustomerFilterCategoryExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private ResultSet mockResultSet;

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
        System.setIn(new ByteArrayInputStream("TestKeyword\nQ".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("TestKeyword");

        assertNotNull(executor.getKeyword());
    }
}

