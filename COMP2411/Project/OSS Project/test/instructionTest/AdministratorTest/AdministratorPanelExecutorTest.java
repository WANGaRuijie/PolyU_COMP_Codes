package instructionTest.AdministratorTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import model.OSS;
import model.instruction.administrator.AdministratorAddProductExecutor;
import model.instruction.administrator.AdministratorPanelExecutor;
import model.instruction.system.QuitExecutor;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;

public class AdministratorPanelExecutorTest {

    private AdministratorPanelExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorPanelExecutor("1234567890");
    }

    @Test
    public void testInformation() {
        executor.information();
        String expectedOutput = "*********************************************************************************\n" +
                "Hello, our distinguished administrator!\n\n" +
                "    [A] Add a new product\n" +
                "    [D] Delete a product\n" +
                "    [E] Edit a product\n" +
                "    [F] Filter the products\n" +
                "    [G1] Get all products\n" +
                "    [S1] Search a product with a product ID\n" +
                "    [S2] Search products with a product name\n" +
                "\n    [G2] Get all customers\n" +
                "    [S3] Search a customer with a phone number\n" +
                "    [S4] Search customers with a user name\n" +
                "\n    [G3] Get all orders\n" +
                "    [S5] Search an order with a order ID\n" +
                "\n    [G4] Get all shopping cart information\n" +
                "    [S6] Search shopping cart information with a product ID\n" +
                "    [S7] Search shopping cart information with a product name\n" +
                "    [S8] Search shopping cart information with a user phone number\n" +
                "\n    [M1] Modify your phone number\n" +
                "    [M2] Modify your user name\n" +
                "    [M3] Modify your password\n" +
                "    [M4] Modify your personal information\n" +
                "\n    [G5] Generate analysis report\n" +
                "\n    [L] Log out\n" +
                "    [Q] Quit\n\n" +
                "Please enter your choice: ";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testExecuteInstruction() throws SQLException {
        setInputScanner("A");

        AdministratorAddProductExecutor mockAddProductExecutor = mock(AdministratorAddProductExecutor.class);
        doNothing().when(mockAddProductExecutor).executeInstruction();

        executor.executeInstruction();

        verify(mockAddProductExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testChoose_invalidInputThenValidInput() throws SQLException {
        setInputScanner("invalid\nA");

        AdministratorAddProductExecutor mockAddProductExecutor = mock(AdministratorAddProductExecutor.class);
        doNothing().when(mockAddProductExecutor).executeInstruction();

        executor.choose("invalid");

        verify(mockAddProductExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testChoose_logout() throws SQLException {
        setInputScanner("L");

        OSS mockOSS = mock(OSS.class);
        doNothing().when(mockOSS).activate();

        executor.choose("L");

        verify(mockOSS, times(1)).activate();
    }

    @Test
    public void testChoose_quit() throws SQLException {
        setInputScanner("Q");

        QuitExecutor mockQuitExecutor = mock(QuitExecutor.class);
        doNothing().when(mockQuitExecutor).executeInstruction();

        executor.choose("Q");

        verify(mockQuitExecutor, times(1)).executeInstruction();
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();
    }

    private void setInputScanner(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }
}

