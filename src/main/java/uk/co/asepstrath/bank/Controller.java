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

        l.add(new Account(50, "Rachel"));
        l.add(new Account(100, "Monica"));
        l.add(new Account(76, "Phoebe"));
        l.add(new Account(BigDecimal.valueOf(23.90), "Joey"));
        l.add(new Account(BigDecimal.valueOf(54.32), "Ross"));
        l.add(new Account(3, "Chandler"));
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
