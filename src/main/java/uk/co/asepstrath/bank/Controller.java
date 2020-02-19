package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Path("/account")
public class Controller {
    private List<Account> l = new ArrayList<>();
    public Controller(){

        l.add(new Account(50.00, "Rachel Green"));
        l.add(new Account(100.00, "Monica Gellar"));
        l.add(new Account(76.00, "Phoebe Buffay"));
        l.add(new Account(23.90, "Joey Tribbiani"));
        l.add(new Account(54.32, "Ross Gellar"));
        l.add(new Account(3.00, "Chandler Bing"));
        l.add(new Account(51500.00, "Barney Stinson"));
        l.add(new Account(330.45, "Ted Mosby"));
        l.add(new Account(756.80, "Lily Aldrin"));
        l.add(new Account(254.00, "Marshall Eriksen"));
        l.add(new Account(1635.52, "Robin Scherbatsky"));

        List<Account> apiAcc = Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asObject(new GenericType<List<Account>>() {
        }).getBody();
        for(int i = 0; i < apiAcc.size(); i++) {
            l.add(apiAcc.get(i));
        }
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

        for(int i = 0; i < l.size(); i++){
            String currency = l.get(i).getCurrency();
            String id = l.get(i).getId();
            String accountType = l.get(i).getAccountType();
            BigDecimal balance = l.get(i).getBalance();
            String name = l.get(i).getName();
            model.put("currency", currency);
            model.put("id",id);
            model.put("accountType", accountType);
            model.put("balance", balance);
            model.put("name",name);
        }
        model.put("accounts", l);

        return new ModelAndView("accounts.hbs", model);

    }
    @GET("/api")
        public String getDataFromWeb() {
        return Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asString().getBody();
    }

}
