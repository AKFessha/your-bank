package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transaction {

    //Instance variable
    private String withdrawAccount;
    private String depositAccount;
    private String timestamp;
    private String id;
    private BigDecimal amount;
    private String currency;

    //Default constructor
    public Transaction(){
    }

    public Transaction(String withdrawAccount, String depositAccount, String timestamp, String id, int amount, String currency) {
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.timestamp = timestamp;
        this.id = id;
        this.amount = BigDecimal.valueOf(amount);
        this.currency = currency;
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
