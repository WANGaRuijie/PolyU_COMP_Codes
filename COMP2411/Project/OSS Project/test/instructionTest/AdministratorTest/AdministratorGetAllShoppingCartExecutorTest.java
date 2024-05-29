package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorGetAllShoppingCartExecutor;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class AdministratorGetAllShoppingCartExecutorTest {

    private AdministratorGetAllShoppingCartExecutor executor;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Before
    public void setUp() {
        executor = new AdministratorGetAllShoppingCartExecutor("1234567890");
    }

    @Test
    public void testInformation() throws SQLException {
        executor.information();
        String expectedOutput = "*********************************************************************************\n" +
                "You are now viewing all of the shopping carts added in the OSS system\n\n";
        assertEquals(expectedOutput, outContent.toString());
    }


    @Test
    public void testChoose() throws SQLException {
        executor.choose("input");
    }

    @Test
    public void testVisitDatabase() throws SQLException {
        executor.visitDatabase();
    }
}

