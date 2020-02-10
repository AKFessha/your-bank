package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import uk.co.asepstrath.bank.example.TestPerson;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Path("/account")
public class Controller {
    private List<Account> l = new ArrayList<>();
    public Controller(){

        l.add(new Account(50, "Rachel"));
        l.add(new Account(100, "Monica"));
        l.add(new Account(76, "Phoebe"));
        l.add(new Account(BigDecimal.valueOf(23.90), "Joey"));
        l.add(new Account(BigDecimal.valueOf(54.32), "Ross"));
        l.add(new Account(3, "Chandler"));
    }

    @GET("/json")
    public String jasonEnd(){
        String json = new Gson().toJson(l );
        return json;
    }

    @GET("/moustache")
    public ModelAndView accounts() {
        Map<String, Object> model = new HashMap<>();

        Account Monica = new Account(100, "Monica");

        Account[] accountsArr = new Account[1];
        accountsArr[0] = Monica;

        model.put("accountsArr", accountsArr);

        return new ModelAndView("accounts.hbs", model);

    }

    @GET("/test")
    public ModelAndView test(){
        Map<String, Object> model = new HashMap<>();

        TestPerson t1 = new TestPerson("John", "Smith");
        TestPerson t2 = new TestPerson("Bob", "Jones");

        TestPerson[] myArray = new TestPerson[2];
        myArray[0] = t1;
        myArray[1] = t2;
        model.put("myArray", myArray);

        return new ModelAndView("test.hbs", model);
    }




}
