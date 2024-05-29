package instructionTest.CustomerTest;

import database.Database;
import model.InputScanner;
import model.instruction.customer.CustomerModifyUserNameExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CustomerModifyUserNameExecutorTest {

    @InjectMocks
    private CustomerModifyUserNameExecutor executor;

    @Mock
    private Database mockDatabase;

    @Mock
    private InputScanner mockInputScanner;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).visitDatabase(); // Avoid actual database interaction
        executor.information();

    }

    @Test
    public void testChoose() throws SQLException {
    }

    @Test
    public void testVisitDatabase() throws SQLException {
    }

    @Test
    public void testGetUsernameValidInput() throws SQLException {
        System.setIn(new ByteArrayInputStream("validUsername\n".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("validUsername");

        String result = executor.getUsername();

        verify(mockInputScanner, times(1)).getInput();
        assert(result.equals("validUsername"));
    }

    @Test
    public void testGetUsernameInvalidInput() throws SQLException {
        System.setIn(new ByteArrayInputStream("invalidUsername\nvalidUsername\n".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("invalidUsername", "validUsername");

        String result = executor.getUsername();

        verify(mockInputScanner, times(2)).getInput();
        assert(result.equals("validUsername"));
    }

    @Test
    public void testGetPersonalUserName() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        ResultSet result = CustomerModifyUserNameExecutor.getPersonalUserName("testPhoneNumber");

        verify(mockDatabase, times(1)).query(anyString());
        assert(result.equals(mockResultSet));
    }
}

