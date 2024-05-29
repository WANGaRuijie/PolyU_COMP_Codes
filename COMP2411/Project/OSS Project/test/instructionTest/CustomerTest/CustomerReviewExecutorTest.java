package instructionTest.CustomerTest;

import database.Database;
import model.instruction.customer.CustomerReviewExecutor;
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

public class CustomerReviewExecutorTest {

    @InjectMocks
    private CustomerReviewExecutor executor;

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
    public void testPrintOrder() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getString("ORDER_ID")).thenReturn("1", "2");
        when(mockResultSet.getString("PRODUCT_ID")).thenReturn("Product1", "Product2");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(3, 4);
        when(mockResultSet.getDouble("COST")).thenReturn(30.0, 60.0);
        when(mockResultSet.getString("SHIPPING_ADDRESS")).thenReturn("Address1", "Address2");

        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        executor.printOrder();
    }

    @Test
    public void testIsExistedOrderID() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        String[] orderIDs = {"1", "2"};

        assertFalse(executor.isExistedOrderID("invalidID"));
        assertTrue(executor.isExistedOrderID("1"));
    }

    @Test
    public void testGetOrderID() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getString("ORDER_ID")).thenReturn("1", "2");

        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        assertArrayEquals(new String[]{"1", "2"}, executor.getOrderID());
    }

    @Test
    public void testGetProductID() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true); // Simulate one row
        when(mockResultSet.getString("PRODUCT_ID")).thenReturn("Product1");

        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        assertEquals("Product1", executor.getProductID("1"));
    }

    @Test
    public void testSqlResultToArray() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction

        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate two rows
        when(mockResultSet.getString(1)).thenReturn("1", "2");

        Object[] resultArray = executor.sqlResultToArray(mockResultSet);

        assertArrayEquals(new String[]{"1", "2"}, resultArray);
    }
}

