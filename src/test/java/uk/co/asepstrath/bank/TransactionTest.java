package uk.co.asepstrath.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;


public class TransactionTest {
    private Transaction t;
    private Controller c;
    private Account WithdrawAcc;
    private Account DepositAcc;

    @BeforeEach
    public void init(){
        c = new Controller();
        t = new Transaction("WID", "DID", "Timestamp", "ID", BigDecimal.valueOf(400), "GBP");
        WithdrawAcc = new Account("WID", "Name", 2456.90, "GBP", "Type");
        DepositAcc = new Account("DID", "Name", 3553.09, "GBP", "Type");

        c.addAccount(WithdrawAcc);
        c.addAccount(DepositAcc);
        c.addTransaction(t);

    }

    @Test
    public void checkForNull(){
        assertNotEquals(null,t);
    }

    @Test
    public void processLegitTransaction(){
        BigDecimal expectedWithdrawBalance = new BigDecimal(2056.90).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal expectedDepositBalance = new BigDecimal(3953.09).setScale(2, RoundingMode.HALF_EVEN);

        c.processTransaction(t.getId());

        assertEquals(expectedWithdrawBalance, WithdrawAcc.getBalance());
        assertEquals(expectedDepositBalance, DepositAcc.getBalance());
    }

    @Test
    public void fraudulentTransaction(){
        BigDecimal expectedWithdrawBalance = new BigDecimal(2456.90).setScale(1, RoundingMode.HALF_EVEN);
        BigDecimal expectedDepositBalance = new BigDecimal(3553.09).setScale(2, RoundingMode.HALF_EVEN);

        c.addFraudTransaction(t);
        c.processTransaction(t.getId());

        assertEquals(expectedWithdrawBalance, WithdrawAcc.getBalance());
        assertEquals(expectedDepositBalance, DepositAcc.getBalance());

    }
}
