package uk.co.asepstrath.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

public class DatabaseTests {

    static Database testingDatabase;
    static String connectionURL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    static String connectionName = "";
    static String connectionPass = "";
    static List<Account> toInsert;

    @BeforeAll
    static void setup() {
        testingDatabase = new Database(connectionURL, connectionName, connectionPass);
        Account testAcc1 = new Account("1", "Craig Murray", 80000.00, "GBP", "Savings Account");
        Account testAcc2 = new Account("2", "x y", 50000.00, "USD", "Personal Loans Account");
        toInsert = new ArrayList<>();
        toInsert.add(testAcc1);
        toInsert.add(testAcc2);
    }

    @Test
    public void testInsert() {
        testingDatabase.insertAccounts(testingDatabase.connection, toInsert);
        ResultSet selectAllResult = testingDatabase.configureQueries(testingDatabase.connection, "SELECT * FROM accounts;");
        int current = 0;
        try {
            while (selectAllResult.next()) {
                assertEquals(toInsert.get(current).getName(), selectAllResult.getString("name")); // assert accounts added with correct values
                current++;
            }
            assertEquals(2, current);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    @Test
    public void testNameQuery() {
        ResultSet nameQueryResult = testingDatabase.configureQueries(testingDatabase.connection, "SELECT * FROM accounts WHERE name = 'Craig Murray';");
        int current = 0;
        try {
            while (nameQueryResult.next()) {
                assertEquals("Craig Murray", nameQueryResult.getString("name")); // assert query returns correct account
                current++;
            }
            assertEquals(1, current); // assert only one account returned
        } catch (SQLException e) {
            e.getMessage();
        }
    }

}
