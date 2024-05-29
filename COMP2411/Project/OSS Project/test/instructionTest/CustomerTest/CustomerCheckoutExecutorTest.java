package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerAddProductExecutor;
import model.instruction.customer.CustomerCheckoutExecutor;
import model.instruction.customer.CustomerDeleteProductExecutor;
import model.instruction.customer.CustomerPanelExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerCheckoutExecutorTest {

    @Mock
    private Database mockDb;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private InputScanner mockScanner;

    private CustomerCheckoutExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        executor = new CustomerCheckoutExecutor("1234567890");
    }

    @Test
    public void informationTest() {
        executor.information();
    }

    @Test
    public void executeInstructionTest() throws SQLException {
        when(mockScanner.getInput()).thenReturn("B");
        CustomerPanelExecutor mockPanelExecutor = mock(CustomerPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();
        executor.executeInstruction();

        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void chooseTest() throws SQLException {
        executor.choose("input");
    }

    @Test
    public void confirmTest() throws SQLException {
        when(mockScanner.getInput()).thenReturn("B");
        CustomerPanelExecutor mockPanelExecutor = mock(CustomerPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();

        executor.confirm();

        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void visitDatabaseTest() throws SQLException {
        when(mockDb.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("TOTAL_SUM")).thenReturn(100.0);

        executor.visitDatabase();
    }

    @Test
    public void getTotalMoneyTest() throws SQLException {
        when(mockDb.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("TOTAL_SUM")).thenReturn(100.0);

        double totalMoney = executor.getTotalMoney();

        assertEquals(100.0, totalMoney, 0.0);
    }

    @Test
    public void getBalanceTest() throws SQLException {
        when(mockDb.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("BALANCE")).thenReturn(500.0);

        double balance = executor.getBalance();

        assertEquals(500.0, balance, 0.0);
    }

    @Test
    public void payTest() throws SQLException {
        when(mockDb.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getDouble("BALANCE")).thenReturn(500.0);

        when(mockScanner.getInput()).thenReturn("Y");
        CustomerPanelExecutor mockPanelExecutor = mock(CustomerPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();

        executor.pay();

        verify(mockDb, times(1)).update(anyString());
        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void proceedOrNotTest() throws SQLException {
        when(mockScanner.getInput()).thenReturn("B");
        CustomerPanelExecutor mockPanelExecutor = mock(CustomerPanelExecutor.class);
        doNothing().when(mockPanelExecutor).executeInstruction();

        executor.proceedOrNot();

        verify(mockPanelExecutor, times(1)).executeInstruction();
    }

    @Test
    public void finalizeCartTest() throws SQLException {
        when(mockScanner.getInput()).thenReturn("N");
        CustomerAddProductExecutor mockAddProductExecutor = mock(CustomerAddProductExecutor.class);
        CustomerDeleteProductExecutor mockDeleteProductExecutor = mock(CustomerDeleteProductExecutor.class);
        doNothing().when(mockAddProductExecutor).executeInstruction();
        doNothing().when(mockDeleteProductExecutor).executeInstruction();

        executor.finalizeCart();

        verify(mockAddProductExecutor, never()).executeInstruction();
        verify(mockDeleteProductExecutor, never()).executeInstruction();
    }

    @Test
    public void getShoppingCartTest() throws SQLException {
        when(mockDb.query(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("PRODUCT_ID")).thenReturn("1");
        when(mockResultSet.getString("PRODUCT_NAME")).thenReturn("Product 1");
        when(mockResultSet.getInt("QUANTITY")).thenReturn(2);
        when(mockResultSet.getDouble("SINGLE_PRICE")).thenReturn(50.0);
    }
}
