package uk.co.asepstrath.bank;

import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonObjectMapper;
import kong.unirest.Unirest;
import uk.co.asepstrath.bank.example.ExampleController;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App extends Jooby {

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule("mem"));

        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/*", "/assets");

        /*
        Now we set up our controllers and their dependencies
         */
        DataSource ds = require(DataSource.class);
        Logger log = getLog();

        mvc(new ExampleController(ds,log));
        mvc(new Controller());

        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());
    }

    public static void main(final String[] args) {
        runApp(args, App::new);
    }

    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");


        // Fetch DB Source
        DataSource ds = require(DataSource.class);
        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            Statement stmt = connection.createStatement();
            //stmt.executeUpdate("CREATE TABLE Example (Key varchar(255),Value varchar(255))");
            //stmt.executeUpdate("INSERT INTO Example " + "VALUES ('WelcomeMessage', 'Welcome to A Bank')");
            String sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
                    + " id SERIAL PRIMARY KEY,\n"
                    + "name VARCHAR(20) NOT NULL,\n"
                    + "balance FLOAT NOT NULL);";
            stmt.execute(sql);
            connection.close();
            String insertion = "INSERT INTO employees (name, balance) "
                    + "VALUES (?,?)";
            PreparedStatement prep = connection.prepareStatement(insertion);
            Controller c = new Controller();
            prep.setString(1, c.l.get(0).getName());
            prep.setBigDecimal(2, c.l.get(0).getBalance());
            String exampleQuery = "SELECT * from accounts";
            ResultSet rs = stmt.executeQuery(exampleQuery);
            System.out.println(rs);
        } catch (SQLException e) {
            log.error("Database Creation Error",e);
        }

    }

    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");
    }

}
