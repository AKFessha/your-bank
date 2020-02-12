package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.annotations.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @GET
    public String Accounts(){
        String json = new Gson().toJson(l );
        return json;
    }





}
