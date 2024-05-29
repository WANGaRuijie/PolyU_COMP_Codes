package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorSearchCustomerOnIDExecutor;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdministratorSearchCustomerOnIDExecutorTest {

    private AdministratorSearchCustomerOnIDExecutor executor;

    @Before
    public void setUp() {
        executor = new AdministratorSearchCustomerOnIDExecutor();
    }

    @Test
    public void testInformation() throws SQLException {
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
    }

    @Test
    public void testChoose() throws SQLException {
    }

    @Test
    public void testVisitDatabase() throws SQLException {
    }

    private ResultSet createMockResultSet() throws SQLException {
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockResultSet.next()).thenReturn(true, false);
        return mockResultSet;
    }
}

