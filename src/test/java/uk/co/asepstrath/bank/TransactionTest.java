package uk.co.asepstrath.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;


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
}
