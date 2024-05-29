package instructionTest.CustomerTest;

import database.Database;
import model.instruction.customer.CustomerGetAllProductsExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CustomerGetAllProductsExecutorTest {

    @InjectMocks
    private CustomerGetAllProductsExecutor executor;

    @Mock
    private Database mockDatabase;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).printAllProductInformation();

        executor.information();

        verify(executor, times(1)).printAllProductInformation();
    }

    @Test
    public void testChoose() throws SQLException {
    }

    @Test
    public void testVisitDatabase() throws SQLException {
    }

    @Test
    public void testPrintAllProductInformation() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDatabase.query(anyString())).thenReturn(mockResultSet);

        executor.printAllProductInformation();

        verify(mockDatabase, times(1)).query(anyString());
    }
}

