package uk.co.asepstrath.bank;

import io.jooby.JoobyTest;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@JoobyTest(App.class)
public class UnitTest {
Controller c;
    @BeforeEach
    public void init(){
        c = new Controller();
    }

    @Test
    public void checkJSONTransactions() throws JSONException {
        assertNotEquals(null, c);
    }



}
