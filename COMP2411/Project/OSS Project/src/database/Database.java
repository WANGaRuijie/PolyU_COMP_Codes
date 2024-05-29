package database;

import java.sql.*;
import oracle.jdbc.driver.*;

public class Database {
    private static final Database database;

    static {
        try {
            database = new Database();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static OracleConnection connection;
    private static final String username = "\"YOUR STUDENT ID\"";
    private static final String pwd = "YOUR ORACLE ACCOUNT PASSWORD";
    private static final String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";

    private Database() throws SQLException {
        try {
            initializeConnection();
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            throw e;
        }
    }



    public static Database getDataBase() {
        return database;
    }

    public void reConnect() {
        try {
            connection.close();
            initializeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeConnection() throws SQLException {
        try {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
            connection = (OracleConnection) DriverManager.getConnection(url, username, pwd);
        } catch (SQLException e) {
            System.out.println("Failed database connection!");
            e.printStackTrace();
            throw e;
        }
    }

    public void closeConnection() throws SQLException {
        try {
            connection.close();
            System.out.println("DB Connection Closed!");
        } catch (SQLException e) {
            System.out.println("Failed to close the connect of the database!");
            e.printStackTrace();
            throw e;
        }
    }

    public void insert(String sql) throws SQLException {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Failed insertion operation!");
            e.printStackTrace();
            throw e;
        }
    }

    public ResultSet query(String sql) throws SQLException {
        ResultSet resultSet;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        }
        catch (SQLException e) {
            System.out.println("Failed query operation!");
            e.printStackTrace();
            throw e;
        }
        return resultSet;
    }

    public void update(String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Failed update!");
            e.printStackTrace();
            throw e;
        }
    }

    public void delete(String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            System.out.println("Failed deletion");
            e.printStackTrace();
            throw e;
        }
    }

    public boolean contains(String table, String attr, String object) throws SQLException {
        String sql = "SELECT * FROM " + table + " WHERE " + attr + " = \'" + object+"\'";
        try{
            ResultSet resultSet = query(sql);
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

}