package uk.co.asepstrath.bank;
import com.google.gson.Gson;
import io.jooby.ModelAndView;
import io.jooby.annotations.*;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/")
public class Controller {

    //Instance variables
    List<Account> allAccounts = new ArrayList<>();
    List<Transaction> allTransactions = new ArrayList<>();
    List<String> allFraudTransactions = new ArrayList<>();

    public Controller() {

        //Retrieve account and transaction data from the API
        List<Account> apiAcc = Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asObject(new GenericType<List<Account>>() {
        }).getBody();
        List<Transaction> apiTran = Unirest.get("https://api.asep-strath.co.uk/api/team2/transactions?PageNumber=1&PageSize=1000").asObject(new GenericType<List<Transaction>>() {
        }).getBody();

        HttpResponse<JsonNode> request = Unirest.get("https://api.asep-strath.co.uk/api/Team2/fraud").header("accept", "application/json").asJson();
        JSONArray responseArray = request.getBody().getArray();

        //Add all fraudulent transactions
        for(int i = 0; i < responseArray.length(); i++){
            allFraudTransactions.add(responseArray.getString(i));
        }

        //Add accounts to List
        for (int i = 0; i < apiAcc.size(); i++) {
            Account current = apiAcc.get(i);
            if(current.getName().matches("[A-Za-z A-Za-z ]+")){
                if (current.getBalance().compareTo(BigDecimal.valueOf(50000)) > 0) {
                    current.setHighProfile();
                    apiAcc.set(i, current);
                }
                current.set2DP();
                allAccounts.add(apiAcc.get(i));
            }
        }

        //Add transactions to List
        for (Transaction current : apiTran) {
            current.set2DP();
            current.setNotFraud();
            allTransactions.add(current);
        }

        processExistingTransactions();
    }

    /*
     * Get a list containing all of the transactions
     */
    public List<Transaction> getAllTransactions(){
        return allTransactions;
    }

    public List<Account> getList(){
        return allAccounts;
    }

      @GET("/json")
      public String jasonEnd(){
            return new Gson().toJson(allAccounts);
        }

        @GET("/jsonTRANS")
        public String JSONTrans(){
            return new Gson().toJson(allTransactions);
        }

        @GET("/account")
        public ModelAndView accounts() {
        Map<String, Object> model = new HashMap<>();

            for (Account allAccount : allAccounts) {
                String currency = allAccount.getCurrency();
                String id = allAccount.getId();
                String accountType = allAccount.getAccountType();
                BigDecimal balance = allAccount.getBalance();
                String name = allAccount.getName();
                String highProfile = allAccount.getHighProfile();
                model.put("highProfile", highProfile);
                model.put("currency", currency);
                model.put("id", id);
                model.put("accountType", accountType);
                model.put("balance", balance);
                model.put("name", name);
            }
        model.put("accounts", allAccounts);

        return new ModelAndView("accounts.hbs", model);

    }
    @GET("/api")
        public String getDataFromWeb() {
        return Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asString().getBody();
    }

    @GET("/transactions")
    public ModelAndView transactions(){

        Map<String, Object> model = new HashMap<>();

        for (Transaction t : allTransactions) {
            String withdrawAccount = t.getWithdrawAccount();
            String depositAccount = t.getDepositAccount();
            String timestamp = t.getTimestamp();
            String id = t.getId();
            BigDecimal amount = t.getAmount();
            String currency = t.getCurrency();
            String fraud = t.getFraud();

            model.put("withdrawAccount", withdrawAccount);
            model.put("depositAccount", depositAccount);
            model.put("timestamp", timestamp);
            model.put("id", id);
            model.put("amount", amount);
            model.put("currency", currency);
            model.put("fraud", fraud);
            model.put("repeat", currency);
        }
        model.put("transactions", allTransactions);
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

    @GET("/repeat")
    public void repeatATransaction(@QueryParam String id){
        repeatTransaction(id);
    }

    public void addAccount(Account toAdd){
        allAccounts.add(toAdd);
    }

    public void addTransaction(Transaction toAdd){
        allTransactions.add(toAdd);
    }

    public Account findAccount(String accountID){
        Account found = null;
        for(Account current: allAccounts){
            if(current.getId().equals(accountID))
                found = current;
        }
        return found;
    }

    public Transaction findTransaction(String transactionID){
        Transaction found = null;
        for(Transaction current: allTransactions){
            if(current.getId().equals(transactionID))
                found = current;
        }
        return found;
    }

    /*
     * Take all transactions from api and apply the deposit/withdraw amounts to each account
     * only if the account is in our bank
     */
    public void processExistingTransactions(){
        for(Transaction current: allTransactions){
            processTransaction(current.getId());
        }
    }

    public void processTransaction(String transactionID) {
        Transaction toProcess = findTransaction(transactionID);

        for(String fraudTransaction: allFraudTransactions){
            if(toProcess.getId().equals(fraudTransaction)){
                toProcess.setYesFraud();
                return;
            }
        }

        Account depAccount = this.findAccount(toProcess.getDepositAccount());
        if(depAccount != null)
            depAccount.deposit(toProcess.getAmount());
        Account withAccount = this.findAccount(toProcess.getWithdrawAccount());
        if(withAccount != null)
            withAccount.withdraw(toProcess.getAmount());
    }

    public String getCurrentTime(){
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    /*
     * @param transactionID - the existing transaction
     */
    public void repeatTransaction(String transactionID){
        Transaction toRepeat = findTransaction(transactionID);
        String newID = UUID.randomUUID().toString();
        Transaction newTransaction = new Transaction(toRepeat.getWithdrawAccount(), toRepeat.getDepositAccount(), getCurrentTime()
                ,newID,toRepeat.getAmount(), toRepeat.getCurrency());
        addTransaction(newTransaction);
        for(String t: allFraudTransactions){
            if(toRepeat.getId().equals(t)) {
                newTransaction.setYesFraud();
                addFraudTransaction(newTransaction);
            }
        }
        processTransaction(newTransaction.getId());
    }

    public void addFraudTransaction(Transaction toAdd){
        allFraudTransactions.add(toAdd.getId());
    }

    /*
     * Compare two BigDecimal numbers
     */
    public int compareTo(BigDecimal bigDecimal) {
        if (this.compareTo(bigDecimal) == 1 || this.compareTo(bigDecimal) == 0)
            return 1;
        else
            return -1;
    }
}
