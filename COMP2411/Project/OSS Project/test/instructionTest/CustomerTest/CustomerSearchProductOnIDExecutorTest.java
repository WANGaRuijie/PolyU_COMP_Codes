package instructionTest.CustomerTest;

import database.Database;
import model.instruction.customer.CustomerSearchProductOnIDExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerSearchProductOnIDExecutorTest {

    @InjectMocks
    private CustomerSearchProductOnIDExecutor executor;

    @Mock
    private Database mockDatabase;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        executor.information();
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("PRODUCT_ID")).thenReturn("Product1");
        when(mockResultSet.getString("PRODUCT_NAME")).thenReturn("Test Product");
        when(mockResultSet.getString("DESCRIPTION")).thenReturn("Test Description");
        when(mockResultSet.getString("SPECIFICATION")).thenReturn("Test Specification");
        when(mockResultSet.getDouble("PRICE")).thenReturn(50.0);
        when(mockResultSet.getInt("AVAILABLE_QUANTITY")).thenReturn(10);
        when(mockResultSet.getString("CATEGORY")).thenReturn("Test Category");
        when(mockResultSet.getString("CONTENT")).thenReturn("Test Content");

        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        executor.visitDatabase();
    }
}

