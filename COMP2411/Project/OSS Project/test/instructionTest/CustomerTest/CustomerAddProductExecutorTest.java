package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerAddProductExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerAddProductExecutorTest {

    private CustomerAddProductExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new CustomerAddProductExecutor("123456");
    }

    @Test
    public void testInformation() {
        executor.information();
        // Assuming information method only prints, no return value to check
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("TestProductID", "2", "Q");

        when(mockDatabase.contains(anyString(), anyString(), anyString())).thenReturn(true);

        System.setIn(new ByteArrayInputStream("TestProductID\n2\nQ".getBytes()));

        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        executor.executeInstruction();

        verify(executor, times(1)).visitDatabase();
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("testInput");

        // Assuming choose method only has console output, no return value to check
    }

    @Test
    public void testGetProductID() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("TestProductID", "invalid", "B");

        System.setIn(new ByteArrayInputStream("TestProductID\ninvalid\nB".getBytes()));

        assertTrue(executor.isExistedProductID(executor.getProductID()));
        assertTrue(executor.isExistedProductID(executor.getProductID()));  // Invalid input, retry
    }

    @Test
    public void testGetQuantity() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("2", "invalid", "B");

        System.setIn(new ByteArrayInputStream("2\ninvalid\nB".getBytes()));

        assertEquals(2, executor.getQuantity());
        assertEquals(2, executor.getQuantity());  // Invalid input, retry
    }

    @Test
    public void testIsExistedProductID() throws SQLException {
        when(mockDatabase.contains(anyString(), anyString(), anyString())).thenReturn(true);

        assertTrue(executor.isExistedProductID("TestProductID"));
    }

    @Test
    public void testIsRepeatedProductID() throws SQLException {
        when(mockDatabase.contains(anyString(), anyString(), anyString())).thenReturn(true);

        assertTrue(executor.isRepeatedProductID("TestProductID"));
    }

    @Test
    public void testGetProductInformation() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString(anyString())).thenReturn("TestProductName");

        assertNotNull(executor.getProductInformation("TestProductID"));
    }

    @Test
    public void testGetProductName() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString(anyString())).thenReturn("TestProductName");

        assertEquals("TestProductName", executor.getProductName());
    }

    @Test
    public void testGetSinglePrice() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getDouble(anyString())).thenReturn(10.0);

        assertEquals(10.0, executor.getSinglePrice(), 0);
    }

    @Test
    public void testIsValidQuantityInput() {
        assertTrue(CustomerAddProductExecutor.isValidQuantityInput("2"));
        assertFalse(CustomerAddProductExecutor.isValidQuantityInput("invalid"));
    }

    @Test
    public void testIsValidQuantity() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getDouble(anyString())).thenReturn(5.0);

        assertTrue(CustomerAddProductExecutor.isValidQuantity(2, "TestProductID"));
        assertFalse(CustomerAddProductExecutor.isValidQuantity(8, "TestProductID"));
    }

    @Test
    public void testAddRepeatedProduct() throws SQLException {
        when(mockInputScanner.getInput()).thenReturn("2");

        System.setIn(new ByteArrayInputStream("2\n".getBytes()));

        executor.addRepeatedProduct();
        verify(mockDatabase, times(1)).update(anyString());
    }
}

