package instructionTest.SystemTest;

import model.instruction.system.QuitExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class QuitExecutorTest {

    private PrintStream originalSystemOut;
    private ByteArrayOutputStream consoleOutput;

    @Before
    public void setUp() {
        originalSystemOut = System.out;
        consoleOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(consoleOutput));
    }

    @After
    public void tearDown() {
        System.setOut(originalSystemOut);
    }

    @Test
    public void testExecuteInstruction() {
        QuitExecutor quitExecutor = new QuitExecutor();

        try {
            quitExecutor.executeInstruction();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        verify(quitExecutor).information();
    }

    @Test
    public void testChoose() {
        QuitExecutor quitExecutor = new QuitExecutor();

        quitExecutor.choose("input");

        verifyZeroInteractions(quitExecutor);
    }

    @Test
    public void testVisitDatabase() {
        QuitExecutor quitExecutor = new QuitExecutor();

        try {
            quitExecutor.visitDatabase();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        verifyZeroInteractions(quitExecutor);
    }

    @Test
    public void testInformation() {
        QuitExecutor quitExecutor = new QuitExecutor();

        String expectedOutput = "*********************************************************************************\n" +
                "Goodbye!\n";

        quitExecutor.information();

        assertEquals(expectedOutput, consoleOutput.toString());
    }
}
