package model;

import java.sql.SQLException;

public interface InstructionExecutor {

    void information() throws SQLException;
    void executeInstruction() throws SQLException;
    void choose(String input) throws SQLException;
    void visitDatabase() throws SQLException;

}
