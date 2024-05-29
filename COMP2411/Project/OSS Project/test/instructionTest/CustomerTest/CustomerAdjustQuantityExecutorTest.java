package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerAdjustQuantityExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomerAdjustQuantityExecutorTest {

    private CustomerAdjustQuantityExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new CustomerAdjustQuantityExecutor("123456");
    }

    @Test
    public void testInformation() {
        executor.information();
    }

    @Test
    public void testConfirm() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("B")
                .thenReturn("Q")
                .thenReturn("anyKey");

        System.setIn(new ByteArrayInputStream("B\nQ\nanyKey\n".getBytes()));

        executor.confirm();
        verify(mockDatabase, times(0)).query(anyString());
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("testInput");
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase();

        executor.executeInstruction();

        verify(executor, times(1)).visitDatabase();
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).printShoppingCart();
        doNothing().when(executor).modifyQuantity();

        executor.visitDatabase();

        verify(executor, times(1)).printShoppingCart();
        verify(executor, times(1)).modifyQuantity();
    }

    @Test
    public void testPrintShoppingCart() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(anyString())).thenReturn("TestProductID").thenReturn("TestProductName");

        executor.printShoppingCart();

        verify(mockDatabase, times(1)).query(anyString());
    }

    @Test
    public void testModifyQuantity() throws SQLException {
        when(mockInputScanner.getInput())
                .thenReturn("TestProductID")
                .thenReturn("2")
                .thenReturn("Q");

        System.setIn(new ByteArrayInputStream("TestProductID\n2\nQ".getBytes()));

        executor.modifyQuantity();
        verify(mockDatabase, times(1)).query(anyString());
    }

    @Test
    public void testIsExistedProductID() throws SQLException {
        when(mockDatabase.contains(anyString(), anyString(), anyString())).thenReturn(true);

        assertTrue(executor.isExistedProductID("TestProductID"));
    }

    @Test
    public void testPrintAvailableQuantity() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyString())).thenReturn(10);

        executor.printAvailableQuantity("TestProductID");

        verify(mockDatabase, times(1)).query(anyString());
    }

    @Test
    public void testSqlResultToArray() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(1)).thenReturn("TestProductID");

        assertNotNull(executor.sqlResultToArray(mockResultSet));
    }

    @Test
    public void testGetQuantity() throws SQLException {
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(1)).thenReturn("5");

        assertNotNull(executor.getQuantity("TestProductID"));
    }
}
