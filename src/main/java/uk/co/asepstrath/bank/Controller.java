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
import java.math.BigInteger;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("/")
public class Controller {

    //Instance variables
    private List<Account> allAccounts = new ArrayList<>();  //Holds every account with our bank
    private List<Transaction> allTransactions = new ArrayList<>();  //Holds every transaction object
    private List<String> allFraudTransactions = new ArrayList<>();  //Holds the ID of all fraudulent transactions
    private BigDecimal highProfileThreshold;    //Threshold for a high profile customer

    public Controller() {

        highProfileThreshold = BigDecimal.valueOf(50000);   //Initially set to 50,000

        //Retrieve account data from the API
        List<Account> apiAcc = Unirest.get("http://api.asep-strath.co.uk/api/Team2/accounts").asObject(new GenericType<List<Account>>() {
        }).getBody();

        //Retrieve transaction data from the API
        List<Transaction> apiTran = Unirest.get("https://api.asep-strath.co.uk/api/team2/transactions?PageNumber=1&PageSize=1000").asObject(new GenericType<List<Transaction>>() {
        }).getBody();

        //Retrieve the fraudulent transaction data from the API
        HttpResponse<JsonNode> request = Unirest.get("https://api.asep-strath.co.uk/api/Team2/fraud").header("accept", "application/json").asJson();
        JSONArray responseArray = request.getBody().getArray();

        //Add all fraudulent transactions to the list
        for(int i = 0; i < responseArray.length(); i++){
            allFraudTransactions.add(responseArray.getString(i));
        }

        //Add all accounts to List
        for (int i = 0; i < apiAcc.size(); i++) {
            Account current = apiAcc.get(i);
            if(current.getName().matches("[A-Za-z A-Za-z ]+")){
                allAccounts.add(apiAcc.get(i));
            }
        }

        //Add all transactions to List
        for (Transaction current : apiTran) {
            current.set2DP();
            current.setNotFraud();
            allTransactions.add(current);
        }

        //Apply all of the API transactions to the appropriate accounts
        processExistingTransactions();
    }

      @GET("/json")
      public String jasonEnd(){
            return new Gson().toJson(allAccounts);
        }

        @GET("/jsonTRANS")
        public String jsonTRANS(){
            return new Gson().toJson(allTransactions);
        }

        @GET("/account")
        public ModelAndView accounts() {
        Map<String, Object> model = new HashMap<>();

            for (Account allAccount : allAccounts) {
                allAccount.setHighProfileFalse();
                if (allAccount.getBalance().compareTo(highProfileThreshold) > 0)
                    allAccount.setHighProfile();

                allAccount.set2DP();
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

    @GET("/admin")
    public ModelAndView admin(){
        return new ModelAndView("admin.hbs");
    }

    @GET("/")
    public ModelAndView home(){
        return new ModelAndView("home.hbs");
    }

    @GET("/configure")
    public ModelAndView configWealthy(){
        return new ModelAndView("configWealthy.hbs");
    }

    @GET("/wealthy")
    public void configWealthyCustomer(@QueryParam int newValue){
        setHighProfileThreshold(newValue);
    }

    @GET("/repeat")
    public void repeatATransaction(@QueryParam String id){
        repeatTransaction(id);
    }

    @GET("/reverse")
    public void reverseATransaction(@QueryParam String id){
        reverseTransaction(id);
    }

    /**
     * Getter method for list of all transactions
     * @return the list of all transactions
     */
    public List<Transaction> getAllTransactions(){
        return allTransactions;
    }

    /**
     * Getter method for list of all accounts
     * @return the list of all accounts
     */
    public List<Account> getList(){
        return allAccounts;
    }

    /**
     * Add an account to the list of all accounts
     * @param toAdd the account to be added
     */
    public void addAccount(Account toAdd){
        allAccounts.add(toAdd);
    }

    /**
     * Add a transaction to the list of all transactions
     * @param toAdd the transaction to be added
     */
    public void addTransaction(Transaction toAdd){
        allTransactions.add(toAdd);
    }

    /**
     * Find an account in our bank
     * @param accountID the String ID of the account to find
     * @return the Account object, null if the account isn't present in the list
     */
    public Account findAccount(String accountID){
        Account found = null;
        for(Account current: allAccounts){
            if(current.getId().equals(accountID))
                found = current;
        }
        return found;
    }

    /**
     * Find a transaction
     * @param transactionID the String ID of the transaction to be found
     * @return the Transaction object, null if the transaction isn't present in the list
     */
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

    /**
     * Take all transactions from API and apply the deposit/withdraw amounts to each account
     * only if the account is in our bank
     */
    public void processExistingTransactions(){
        for(Transaction current: allTransactions){
            processTransaction(current.getId());
        }
    }

    /**
     * Process a single transaction
     * @param transactionID the String ID of the transaction to be applied. This includes withdraw
     *                      and deposit where an account is in our bank
     */
    public void processTransaction(String transactionID) {
        Transaction toProcess = findTransaction(transactionID);     //Get the transaction the user wants to process

        //If the transaction couldn't be found, don't continue
        if(toProcess == null)
            return;

        //Check if the transaction is fraudulent, exit if yes, otherwise continue
        if(isFraudulent(toProcess.getId())){
            toProcess.setYesFraud();
            return;
        }

        //Only deposit/withdraw if the account is with our bank (exists in the list of all accounts
        Account depAccount = this.findAccount(toProcess.getDepositAccount());
        if(depAccount != null)
            depAccount.deposit(toProcess.getAmount());

        Account withAccount = this.findAccount(toProcess.getWithdrawAccount());
        if(withAccount != null)
            withAccount.withdraw(toProcess.getAmount());
    }

    /**
     * Get a timestamp of the instant of time this method was called. Format is in microseconds
     * @return The timestamp in the provided microsecond format
     */
    public String getCurrentTime(){
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    /**
     * Get a timestamp of the instant of time this method was called. Format is in microseconds.
     * This is appropriate for transaction reversal
     * @return the timestamp in a format acceptable for transaction reversal
     */
    public String getReversalCurrentTime(){
        Instant instant = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }

    /**
     * Repeat a transaction. Same deposit/withdraw accounts are used. A new transaction is created and
     * added to the list of all transactions. This will have a new ID and timestamp.
     * @param transactionID the ID of the transaction to be repeated
     */
    public void repeatTransaction(String transactionID){

        Transaction toRepeat = findTransaction(transactionID);  //Find the transaction
        if(toRepeat == null || isFraudulent(toRepeat.getId()))
            return;

        String newID = UUID.randomUUID().toString();    //Generate a unique ID
        Transaction newTransaction = new Transaction(toRepeat.getWithdrawAccount(), toRepeat.getDepositAccount(), getCurrentTime()
                ,newID,toRepeat.getAmount(), toRepeat.getCurrency());
        addTransaction(newTransaction);

        processTransaction(newTransaction.getId());
    }

    /**
     * Reverse a transaction. Essentially swap the deposit and withdraw accounts. The transaction
     * will be deleted from the list of all transactions
     * @param transactionID the ID of the transaction to be reversed
     */
    public void reverseTransaction(String transactionID){

        Transaction toReverse = findTransaction(transactionID);  //Find the transaction
        if(toReverse == null || isFraudulent(toReverse.getId()))
            return;

        Account newDeposit = findAccount(toReverse.getWithdrawAccount());
        Account newWithdraw = findAccount(toReverse.getDepositAccount());

        if(newDeposit !=null)
            newDeposit.deposit(toReverse.getAmount());
        if(newWithdraw != null)
            newWithdraw.withdraw(toReverse.getAmount());

        allTransactions.remove(toReverse);

        //Notify other banks that a transaction regarding one of their accounts was reversed
        if(newDeposit == null || newWithdraw == null)
            notifyOtherBankOfReversal(transactionID);

    }

    /**
     * Notify another bank that a transaction reversal has taken place regarding one of
     * their accounts
     * @param transactionID the ID of the transaction that was reversed
     */
    public int notifyOtherBankOfReversal(String transactionID){

        HttpResponse response =
                Unirest.post("https://api.asep-strath.co.uk/api/Team2/reversal")
                .header("accept", "*/*")
                .header("Content-Type", "application/json")
                .body("{\"transaction\": "+ "\"" + transactionID + "\",\"timestamp\": " + "\"" + getReversalCurrentTime() +
                "\"}")
                .asJson();

        return response.getStatus();
    }

    /**
     * Check if a given transaction is fraudulent
     * @param transactionID the String ID of the transaction to check
     * @return true if the transaction ID exists in the fraudulent list, otherwise false
     */
    public boolean isFraudulent(String transactionID){
        for(String t: allFraudTransactions){
            if(transactionID.equals(t))
                return true;
        }
        return false;
    }

    /**
     * Mark a transaction as fraudulent
     * @param toAdd the transaction to mark
     */
    public void addFraudTransaction(Transaction toAdd){
        allFraudTransactions.add(toAdd.getId());
    }

    /**
     * Compare if a BigDecimal number is >= to THIS
     * @param bigDecimal the value to compare to THIS
     * @return 1 if the number is >= to THIS, otherwise -1
     */
    public int compareTo(BigDecimal bigDecimal) {
        if (this.compareTo(bigDecimal) == 1 || this.compareTo(bigDecimal) == 0)
            return 1;
        else
            return -1;
    }

    /**
     * Retrieve the current set value for a wealthy customer
     * @return the threshold for considering a customer high profile
     */
    public BigDecimal getHighProfileThreshold(){
        return highProfileThreshold;
    }

    /**
     * Set the threshold value for a customer to be considered wealthy
     * @param value new value for considering a customer high profile
     */
    public void setHighProfileThreshold(int value){
        highProfileThreshold = BigDecimal.valueOf(value);
    }
}
