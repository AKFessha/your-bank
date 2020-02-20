package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    private String id;
    private String name;
    private BigDecimal balance;
    private String currency;
    private String accountType;
    private String highProfile = "No";

    public Account(){
        this.balance = BigDecimal.ZERO;
    }

    public Account(String id, String name, double balance, String currency, String accountType){
        this.id = id;
        this.name = name;
        this.balance = BigDecimal.valueOf(balance);
        this.currency = currency;
        this.accountType = accountType;
    }

    public void setHighProfile(){
        if(accountType.equals("Savings Account") || accountType.equals("Checking Account")
            || accountType.equals("Money Market Account") || accountType.equals("Investment Account")) {
            highProfile = "Yes";
        }
    }

    public String getHighProfile(){
        return highProfile;
    }

    public String getName(){
        return name;
    }

    public Account(BigDecimal startingBalance, String n){
        this.balance = startingBalance;
        this.name = n;
    }
    public Account(double startingBal, String n){
        this.balance = BigDecimal.valueOf(startingBal);
        this.name = n;
    }

    public void set2DP(){
        balance = balance.setScale(2, RoundingMode.HALF_EVEN);
    }

    public String getCurrency(){
        return currency;
    }

    public String getId(){
        return id;
    }

    public String getAccountType(){
        return accountType;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    public void deposit(int amount) {
        this.balance = this.balance.add(BigDecimal.valueOf(amount));
    }

    public void deposit(double amount){
        this.balance = this.balance.add(BigDecimal.valueOf(amount));
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void withdraw(BigDecimal ammount) throws ArithmeticException{
        if(this.balance.compareTo(ammount) == -1){
            throw new ArithmeticException();
        }else {
           this.balance =  this.balance.subtract(ammount);
        }
    }

    public void withdraw(int ammount) throws ArithmeticException{
        if(this.balance.compareTo(BigDecimal.valueOf(ammount)) == -1){
            throw new ArithmeticException();
        }else {
            this.balance =  this.balance.subtract(BigDecimal.valueOf(ammount));
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "id ='" + id + '\'' +
                ", name='" + name + '\'' +
                ", bal=" + balance +
                ", currency ='" + currency + '\'' +
                ", accountType ='" + accountType + '\'' +
                '}';
    }
}
