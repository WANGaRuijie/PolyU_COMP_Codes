package instructionTest.CustomerTest;

import model.InputScanner;
import model.instruction.customer.*;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CustomerFilterExecutorTest {

    @InjectMocks
    private CustomerFilterExecutor executor;

    @Mock
    private CustomerFilterNameExecutor mockFilterNameExecutor;

    @Mock
    private CustomerFilterDescriptionExecutor mockFilterDescriptionExecutor;

    @Mock
    private CustomerFilterSpecificationExecutor mockFilterSpecificationExecutor;

    @Mock
    private CustomerFilterPriceExecutor mockFilterPriceExecutor;

    @Mock
    private CustomerFilterCategoryExecutor mockFilterCategoryExecutor;

    @Mock
    private CustomerPanelExecutor mockPanelExecutor;

    @Mock
    private QuitExecutor mockQuitExecutor;

    @Mock
    private InputScanner mockInputScanner;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        executor = spy(executor);
        doNothing().when(executor).information();

        System.setIn(new ByteArrayInputStream("F1".getBytes()));
        when(mockInputScanner.getInput()).thenReturn("F1");

        doNothing().when(mockFilterNameExecutor).executeInstruction();

        executor.executeInstruction();

        verify(mockFilterNameExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testChoose() throws SQLException {
        executor.choose("F1");
        verify(mockFilterNameExecutor, times(1)).executeInstruction();

        executor.choose("F2");
        verify(mockFilterDescriptionExecutor, times(1)).executeInstruction();

        executor.choose("F3");
        verify(mockFilterSpecificationExecutor, times(1)).executeInstruction();

        executor.choose("F4");
        verify(mockFilterPriceExecutor, times(1)).executeInstruction();

        executor.choose("F5");
        verify(mockFilterCategoryExecutor, times(1)).executeInstruction();

        executor.choose("B");
        verify(mockPanelExecutor, times(1)).executeInstruction();

        executor.choose("Q");
        verify(mockQuitExecutor, times(1)).executeInstruction();

    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();
    }
}

