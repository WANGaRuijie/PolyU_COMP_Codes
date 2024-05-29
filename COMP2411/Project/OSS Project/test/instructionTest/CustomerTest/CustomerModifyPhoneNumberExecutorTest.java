package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerModifyPhoneNumberExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerModifyPhoneNumberExecutorTest {

    private CustomerModifyPhoneNumberExecutor executor;
    private InputScanner mockScanner;
    private Database mockDb;

    @Before
    public void setUp() {
        executor = new CustomerModifyPhoneNumberExecutor("1234567890");
        mockScanner = mock(InputScanner.class);
        mockDb = mock(Database.class);
        executor.setScanner(mockScanner);
        executor.setDatabase(mockDb);
    }

    @Test
    public void informationTest() throws SQLException {
        executor.information();
    }

    @Test
    public void visitDatabaseTest() throws SQLException {
        executor.visitDatabase();
    }

    @Test
    public void getPhoneNumberTest_validInput() throws SQLException {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("newPhoneNumber\n".getBytes());
        System.setIn(in);

        when(mockScanner.getInput()).thenReturn("newPhoneNumber");

        String phoneNumber = executor.getPhoneNumber();

        assertEquals("newPhoneNumber", phoneNumber);

        System.setIn(sysInBackup);
    }

    @Test
    public void getPhoneNumberTest_invalidInput() throws SQLException {
        InputStream sysInBackup = System.in;
        ByteArrayInputStream in = new ByteArrayInputStream("invalidPhoneNumber\nnewPhoneNumber\n".getBytes());
        System.setIn(in);

        when(mockScanner.getInput())
                .thenReturn("invalidPhoneNumber")
                .thenReturn("newPhoneNumber");

        String phoneNumber = executor.getPhoneNumber();

        assertEquals("newPhoneNumber", phoneNumber);

        System.setIn(sysInBackup);
    }

    @Test
    public void getPersonalPhoneNumberTest() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDb.query(anyString())).thenReturn(mockResultSet);

        ResultSet result = executor.getPersonalPhoneNumber("1234567890");

        assertEquals(mockResultSet, result);
    }
}
