package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import kong.unirest.Unirest;


import java.math.BigDecimal;
import java.util.*;

@Path("/account")
public class Controller {
    private List<Account> l = new ArrayList<>();
    public Controller(){

        l.add(new Account(BigDecimal.valueOf(50.00), "Rachel Green"));
        l.add(new Account(BigDecimal.valueOf(100.00), "Monica Gellar"));
        l.add(new Account(BigDecimal.valueOf(76.00), "Phoebe Buffay"));
        l.add(new Account(BigDecimal.valueOf(23.90), "Joey Tribbiani"));
        l.add(new Account(BigDecimal.valueOf(54.32), "Ross Gellar"));
        l.add(new Account(BigDecimal.valueOf(3.00), "Chandler Bing"));
        l.add(new Account(BigDecimal.valueOf(51500.00), "Barney Stinson"));
        l.add(new Account(BigDecimal.valueOf(330.45), "Ted Mosby"));
        l.add(new Account(BigDecimal.valueOf(756.80), "Lily Aldrin"));
        l.add(new Account(BigDecimal.valueOf(254.00), "Marshall Eriksen"));
        l.add(new Account(BigDecimal.valueOf(1635.52), "Robin Scherbatsky"));
    }

    public List<Account> getList(){
        return l;
    }

      @GET("/json")

      public String jasonEnd(){
            String json = new Gson().toJson(l );
            return json;
        }


        @GET("/VFA")
    public ModelAndView accounts() {
        Map<String, Object> model = new HashMap<>();

        model.put("accounts", l);

        return new ModelAndView("accounts.hbs", model);

    }
    @GET("/api")
    public String getDataFromWeb() {
        return  Unirest.get("http://api.asep-strath.co.uk/api/team2/accounts").asString().getBody();
    }

}
