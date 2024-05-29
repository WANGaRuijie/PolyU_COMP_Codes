package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerDeleteProductExecutor;
import model.instruction.customer.CustomerPanelExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CustomerDeleteProductExecutorTest {

    private CustomerDeleteProductExecutor executor;
    private InputScanner mockScanner;
    private Database mockDb;

    @Before
    public void setUp() {
        executor = new CustomerDeleteProductExecutor("1234567890");
        mockScanner = mock(InputScanner.class);
        mockDb = mock(Database.class);
        executor.setScanner(mockScanner);
        executor.setDatabase(mockDb);
    }

    @Test
    public void informationTest() {
        executor.information();
    }

    @Test
    public void executeInstructionTest_productIDInput() throws SQLException {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("validProductID\n".getBytes());
        System.setIn(in);

        when(mockScanner.getInput()).thenReturn("validProductID");
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "validProductID")).thenReturn(true);

        executor.executeInstruction();

        verify(mockDb, times(1)).insert(anyString());

        System.setIn(sysInBackup);
    }

    @Test
    public void executeInstructionTest_invalidProductIDInput() throws SQLException {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("invalidProductID\nvalidProductID\n".getBytes());
        System.setIn(in);

        when(mockScanner.getInput())
                .thenReturn("invalidProductID")
                .thenReturn("validProductID");
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "invalidProductID")).thenReturn(false);
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "validProductID")).thenReturn(true);

        executor.executeInstruction();

        verify(mockDb, times(1)).insert(anyString());

        System.setIn(sysInBackup);
    }

    @Test
    public void getProductIDTest_validProductID() throws SQLException {
        when(mockScanner.getInput()).thenReturn("validProductID");
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "validProductID")).thenReturn(true);

        String productID = executor.getProductID();

        assertEquals("validProductID", productID);
    }

    @Test
    public void getProductIDTest_invalidProductID() throws SQLException {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("invalidProductID\nvalidProductID\n".getBytes());
        System.setIn(in);

        when(mockScanner.getInput())
                .thenReturn("invalidProductID")
                .thenReturn("validProductID");
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "invalidProductID")).thenReturn(false);
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "validProductID")).thenReturn(true);

        String productID = executor.getProductID();

        assertEquals("validProductID", productID);

        System.setIn(sysInBackup);
    }

    @Test
    public void isExistedProductIDTest_existingProductID() throws SQLException {
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "validProductID")).thenReturn(true);

        boolean result = executor.isExistedProductID("validProductID");

        assertTrue(result);
    }

    @Test
    public void isExistedProductIDTest_nonExistingProductID() throws SQLException {
        when(mockDb.contains("PRODUCT", "PRODUCT_ID", "invalidProductID")).thenReturn(false);

        boolean result = executor.isExistedProductID("invalidProductID");

        assertFalse(result);
    }

    @Test
    public void visitDatabaseTest() throws SQLException {
        when(mockDb.getDataBase()).thenReturn(mockDb);

        executor.visitDatabase();

        verify(mockDb, times(1)).insert(anyString());
    }
}
