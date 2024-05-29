package model.instruction.system;

import model.InstructionExecutor;

import java.sql.SQLException;

public class QuitExecutor implements InstructionExecutor {

    @Override
    public void information() {
        System.out.println("*********************************************************************************");
        System.out.println("Goodbye!");
    }
    @Override
    public void executeInstruction() {
        information();
        System.exit(0);
    }

    public void choose(String input) {}

    @Override
    public void visitDatabase() throws SQLException {}
}
