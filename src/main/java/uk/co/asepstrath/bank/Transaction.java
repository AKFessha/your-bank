package uk.co.asepstrath.bank;

import kong.unirest.GenericType;
import kong.unirest.Unirest;

import java.awt.font.TransformAttribute;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Transaction{

    //Instance variable
    private String withdrawAccount;
    private String depositAccount;
    private String timestamp;
    private String id;
    private BigDecimal amount;
    private String currency;
    private String fraud;

    //Default constructor
    public Transaction(){
    }

    public Transaction(String withdrawAccount, String depositAccount, String timestamp, String id, BigDecimal amount, String currency) {
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.timestamp = timestamp;
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        fraud = "No";
    }

    public String getFraud(){
        return fraud;
    }

    public void setYesFraud(){
        this.fraud = "Yes";
    }

    public void setNotFraud(){
        this.fraud = "No";
    }

    public String getWithdrawAccount() {
        return withdrawAccount;
    }

    public void setWithdrawAccount(String withdrawAccount) {
        this.withdrawAccount = withdrawAccount;
    }

    public String getDepositAccount() {
        return depositAccount;
    }

    public void setDepositAccount(String depositAccount) {
        this.depositAccount = depositAccount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = BigDecimal.valueOf(amount);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void set2DP(){
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

}
