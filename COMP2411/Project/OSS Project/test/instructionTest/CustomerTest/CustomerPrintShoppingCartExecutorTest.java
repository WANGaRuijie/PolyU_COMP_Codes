package instructionTest.CustomerTest;

import database.Database;
import model.instruction.customer.CustomerPrintShoppingCartExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CustomerPrintShoppingCartExecutorTest {

    @InjectMocks
    private CustomerPrintShoppingCartExecutor executor;

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
    public void testPrintShoppingCart() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getString("PRODUCT_ID")).thenReturn("1", "2");
        when(mockResultSet.getString("PRODUCT_NAME")).thenReturn("Product1", "Product2");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(3, 4);
        when(mockResultSet.getDouble("SINGLE_PRICE")).thenReturn(10.0, 15.0);
        when(mockResultSet.getDouble("TOTAL_PRICE")).thenReturn(30.0, 60.0);

        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        executor.printShoppingCart();
    }
}
