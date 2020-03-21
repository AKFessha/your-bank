package uk.co.asepstrath.bank;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTests {

    @Test
    public void createAccount(){
        Account a = new Account();
        assertTrue(a != null);
    }

    @Test
    public void newAcc0(){
        Account a = new Account();
        assertEquals(BigDecimal.ZERO, a.getBalance());
    }

    @Test
    public void dep50I20(){
        Account a = new Account("1","John Doe",0,"Pounds","Savings Account");
        a.deposit(20);
        a.deposit(50);
        assertEquals(BigDecimal.valueOf(70.0), a.getBalance());
    }

    @Test
    public void with20f40(){
        Account a = new Account("1","John Doe",0,"Pounds","Savings Account");
        a.deposit(40);
        a.withdraw(20);
        assertEquals(BigDecimal.valueOf(20.0), a.getBalance());
    }

    @Test
    public void noOverdraft(){
        Account a = new Account("1","John Doe",0,"Pounds","Savings Account");
        a.deposit(30);
        assertThrows(ArithmeticException.class, () -> a.withdraw(100));
    }

    @Test
    public void superSaver(){
        Account a = new Account(BigDecimal.valueOf(20), "John Doe");
        a.deposit(10);
        a.deposit(10);
        a.deposit(10);
        a.deposit(10);
        a.deposit(10);

        a.withdraw(20);
        a.withdraw(20);
        a.withdraw(20);

        assertEquals(BigDecimal.valueOf(10), a.getBalance());
    }

    @Test
    public void pennies(){
        Account a = new Account(BigDecimal.valueOf(5.45), "John Doe");
        a.deposit(17.56);
        assertEquals(BigDecimal.valueOf(23.01),a.getBalance());

    }

    @Test
    public void HighProfileAccount(){
        Account a = new Account("1","Jack Donnelly",150.50,"GDP","Investment Account");
        a.setHighProfile();
        assertEquals("Yes",a.getHighProfile());
    }

    @Test
    public void CheckToString(){
        String exp = "Account{" +
                "id ='1'" +
                ", name='Jack'" +
                ", bal=150.05" +
                ", currency ='GDP'" +
                ", accountType ='Investment Account'" +
                "}";
        Account a = new Account("1","Jack",150.05,"GDP","Investment Account");
        assertEquals(0,exp.compareTo(a.toString()));
    }

    @Test
    public void highValue(){
        Account a = new Account();
        Controller c = new Controller();
        boolean highValue = false;
        c.setHighProfileThreshold(500000);
        a.deposit(1000000);
        if(a.getBalance().compareTo(c.getHighProfileThreshold()) > 0)
            highValue = true;
        assertTrue(highValue);
    }

}
