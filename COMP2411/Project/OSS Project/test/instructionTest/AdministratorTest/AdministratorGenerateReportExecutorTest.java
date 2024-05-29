package instructionTest.AdministratorTest;

import model.instruction.administrator.AdministratorGenerateReportExecutor;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class AdministratorGenerateReportExecutorTest {

    @Test
    public void testInformation() {
        AdministratorGenerateReportExecutor executor = new AdministratorGenerateReportExecutor("123");
        try {
            executor.information();
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }

    @Test
    public void testExecuteInstruction() {
        AdministratorGenerateReportExecutor executor = new AdministratorGenerateReportExecutor("123");
        try {
            executor.executeInstruction();
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }

    @Test
    public void testChoose() {
        AdministratorGenerateReportExecutor executor = new AdministratorGenerateReportExecutor("123");
        String input = "testInput";
        try {
            executor.choose(input);
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }

    @Test
    public void testVisitDatabase() {
        AdministratorGenerateReportExecutor executor = new AdministratorGenerateReportExecutor("123");
        try {
            executor.visitDatabase();
        } catch (SQLException e) {
            fail("Unexpected SQLException");
        }
    }
}
