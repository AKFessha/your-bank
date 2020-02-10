package uk.co.asepstrath.bank;

import java.math.BigDecimal;
import java.util.List;

public class Account {
    private BigDecimal bal;
    private String name;

    public Account(){
        this.bal = BigDecimal.ZERO;
        this.name = "John Doe";
    }

    public String getName(){
        return name;
    }

    public Account(BigDecimal startingBalance, String n){
        this.bal = startingBalance;
        this.name = n;
    }
    public Account(int startingBal, String n){
        this.bal = BigDecimal.valueOf(startingBal);
        this.name = n;
    }

    public void deposit(BigDecimal amount) {
        this .bal = this.bal.add(amount);
    }
    public void deposit(int amount) {
        this .bal = this.bal.add(BigDecimal.valueOf(amount));
    }

    public BigDecimal getBalance() {
        return this.bal;
    }

    public void withdraw(BigDecimal ammount) throws ArithmeticException{
        if(this.bal.compareTo(ammount) == -1){
            throw new ArithmeticException();
        }else {
           this.bal =  this.bal.subtract(ammount);
        }
    }

    public void withdraw(int ammount) throws ArithmeticException{
        if(this.bal.compareTo(BigDecimal.valueOf(ammount)) == -1){
            throw new ArithmeticException();
        }else {
            this.bal =  this.bal.subtract(BigDecimal.valueOf(ammount));
        }
    }

    @Override
    public String toString() {
        return "Account{" +
                "bal=" + bal +
                ", name='" + name + '\'' +
                '}';
    }
}
