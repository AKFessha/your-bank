package uk.co.asepstrath.bank;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class Database {

    DataSource ds;
    Connection connection;
    String connectionURL;
    String connectionName;
    String connectionPass;

    public Database(DataSource ds) {
        this.ds = ds;
        // Open Connection to DB
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            e.getMessage();
        }

        createTable(connection);
        Controller controller = new Controller();
        List<Account> accountsList = controller.getList();
        insertAccounts(connection, accountsList);
        configureQueries(connection, "SELECT * FROM accounts;");
        closeConnection(connection);
    }

    public Database(String connectionURL, String connectionName, String connectionPass) { // local connection for testing purposes
        this.connectionURL = connectionURL;
        this.connectionName = connectionName;
        this.connectionPass = connectionPass;
        try {
            connection = DriverManager.getConnection(connectionURL, connectionName, connectionPass);
        } catch (SQLException e) {
            e.getMessage();
        }
        createTable(connection);
    }

    public void createTable(Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id SERIAL PRIMARY KEY,\n"
                    + " name VARCHAR(20) NOT NULL,\n"
                    + " balance DECIMAL NOT NULL,\n"
                    + " currency VARCHAR(20),\n"
                    + " accountType VARCHAR(30),\n"
                    + " highProfile VARCHAR(3));";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void insertAccounts(Connection connection, List<Account> accountsList) {
        String insertions = "INSERT INTO accounts (name, balance, currency, accountType, highProfile) "
                + "VALUES (?,?,?,?,?);";
        try (PreparedStatement prep = connection.prepareStatement(insertions);) {
            for (int i = 0; i < accountsList.size(); i++) {
                prep.setString(1, accountsList.get(i).getName());
                prep.setBigDecimal(2, accountsList.get(i).getBalance());
                prep.setString(3, accountsList.get(i).getCurrency());
                prep.setString(4, accountsList.get(i).getAccountType());
                prep.setString(5, accountsList.get(i).getHighProfile());
                prep.executeUpdate();
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public ResultSet configureQueries(Connection connection, String query) {
        try (PreparedStatement prepQuery = connection.prepareStatement(query)) {
            return prepQuery.executeQuery();
        } catch (SQLException e) {
            e.getMessage();
            return null;
        }
    }

    public void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
