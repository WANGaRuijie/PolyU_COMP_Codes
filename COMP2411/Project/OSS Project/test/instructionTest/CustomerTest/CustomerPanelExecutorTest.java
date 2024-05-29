package instructionTest.CustomerTest;

import model.instruction.customer.CustomerPanelExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

public class CustomerPanelExecutorTest {

    @InjectMocks
    private CustomerPanelExecutor executor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() {
        executor = spy(executor);
        try {
            doNothing().when(executor).visitDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        executor.information();

    }

    @Test
    public void testVisitDatabase() throws SQLException {
    }
}


