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

@Path("/")
public class Controller implements Comparable<BigDecimal>{
    private List<Account> l = new ArrayList<>();
    public Controller(){

        List<Account> apiAcc = Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asObject(new GenericType<List<Account>>() {
        }).getBody();
        for(int i = 0; i < apiAcc.size(); i++) {
            Account current = apiAcc.get(i);
            if(current.getBalance().compareTo(BigDecimal.valueOf(50000))==1){
                current.setHighProfile();
                apiAcc.set(i, current);
            }
            current.set2DP();
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


    @GET("/accounts")
    public ModelAndView accounts() {
        Map<String, Object> model = new HashMap<>();

        for(int i = 0; i < l.size(); i++){
            String currency = l.get(i).getCurrency();
            String id = l.get(i).getId();
            String accountType = l.get(i).getAccountType();
            BigDecimal balance = l.get(i).getBalance();
            String name = l.get(i).getName();
            String highProfile = l.get(i).getHighProfile();
            model.put("highProfile", highProfile);
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

    @Override
    public int compareTo(BigDecimal bigDecimal) {
        if (this.compareTo(bigDecimal) == 1 || this.compareTo(bigDecimal) == 0)
            return 1;
        else
            return -1;
    }

}
