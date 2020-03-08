package uk.co.asepstrath.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {
    private Transaction t;
    @BeforeEach
    public void init(){
        t = new Transaction();
    }

    @Test
    public void checkForNull(){
        assertNotEquals(null,t);
    }

    @Test
    public void processLegitTransaction(){
        Controller c = new Controller();
        Account WithdrawAcc = new Account("WID", "Name", 2456.90, "GBP", "Type");
        Account DepositAcc = new Account("DID", "Name", 3553.09, "GBP", "Type");
        Transaction t = new Transaction("WID", "DID", "Timestamp", "ID", 400, "GBP");

        c.addAccount(WithdrawAcc);
        c.addAccount(DepositAcc);
        c.addTransaction(t);
        c.processTransaction(t.getId());

        BigDecimal expectedWithdrawBalance = new BigDecimal(2056.90).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal expectedDepositBalance = new BigDecimal(3953.09).setScale(2, RoundingMode.HALF_EVEN);

        assertEquals(expectedWithdrawBalance, WithdrawAcc.getBalance());
        assertEquals(expectedDepositBalance, DepositAcc.getBalance());
    }
}
