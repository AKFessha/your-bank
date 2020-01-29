package uk.co.asepstrath.bank;

import java.math.BigDecimal;

public class Account {
    private BigDecimal bal;

    public Account(){
        this.bal = BigDecimal.ZERO;
    }

    public Account(BigDecimal startingBalance){
        this.bal = startingBalance;
    }

    public void deposit(BigDecimal amount) {
        this .bal = this.bal.add(amount);
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
}
