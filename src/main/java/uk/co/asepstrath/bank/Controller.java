package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import kong.unirest.GenericType;
import kong.unirest.Unirest;


import java.math.BigDecimal;
import java.util.*;

@Path("/")
public class Controller {

    //Instance variables
    List<Account> l = new ArrayList<>();
    List<Transaction> t = new ArrayList<>();

    public Controller() {

        //Retrieve account and transaction data from the API
        List<Account> apiAcc = Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asObject(new GenericType<List<Account>>() {
        }).getBody();
        List<Transaction> apiTran = Unirest.get("http://api.asep-strath.co.uk/api/Team2/transactions").asObject(new GenericType<List<Transaction>>() {
        }).getBody();

        //Add accounts to List
        for (int i = 0; i < apiAcc.size(); i++) {
            Account current = apiAcc.get(i);
            if(current.getBalance().compareTo(BigDecimal.valueOf(50000))>0){
                current.setHighProfile();
                apiAcc.set(i, current);
            }
            current.set2DP();
            l.add(apiAcc.get(i));
        }

        //Add transactions to List
        for (int j = 0; j < apiTran.size(); j++) {
            Transaction current = apiTran.get(j);
            current.set2DP();
            t.add(current);
        }
    }

    public List<Account> getList(){
        return l;
    }

      @GET("/json")
      public String jasonEnd(){
            return new Gson().toJson(l);
        }

        @GET("/jsonTRANS")
        public String JSONTrans(){
            return new Gson().toJson(t);
        }

        @GET("/account")
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

    @GET("/transactions")
    public ModelAndView transactions(){

        Map<String, Object> model = new HashMap<>();

        for (int i = 0; i <t.size(); i++){
            String withdrawAccount = t.get(i).getWithdrawAccount();
            String depositAccount = t.get(i).getDepositAccount();
            String timestamp = t.get(i).getTimestamp();
            String id = t.get(i).getId();
            BigDecimal amount = t.get(i).getAmount();
            String currency = t.get(i).getCurrency();
            model.put("withdrawAccount", withdrawAccount);
            model.put("depositAccount", depositAccount);
            model.put("timestamp", timestamp);
            model.put("id", id);
            model.put("amount", amount);
            model.put("currency", currency);
    }
        model.put("transactions", t);
        return new ModelAndView("transactions.hbs", model);
}

    @GET("/savings")
    public ModelAndView savings(){
        return new ModelAndView("savings.hbs");
    }

    @GET("/")
    public ModelAndView home(){
        return new ModelAndView("home.hbs");
    }


    public int compareTo(BigDecimal bigDecimal) {
        if (this.compareTo(bigDecimal) == 1 || this.compareTo(bigDecimal) == 0)
            return 1;
        else
            return -1;
    }
}
