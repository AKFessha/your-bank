package uk.co.asepstrath.bank;

import io.jooby.MockRouter;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnitTest {

    @Test
    public void theList(){
        Controller c = new Controller();
        String test = c.getList().toString();
        MockRouter router = new MockRouter(new App());
        router.get("/account/list", rsp -> {
            assertEquals(test, rsp.value());
            assertEquals(StatusCode.OK, rsp.getStatusCode());
        });
    }

    @Test
    public void json(){
        Controller c = new Controller();
        String test = c.jasonEnd();
        MockRouter router = new MockRouter(new App());
        router.get("/account/json", rsp -> {
            assertEquals(test, rsp.value());
            assertEquals(StatusCode.OK, rsp.getStatusCode());
        });
    }

   /* @Test
    public void moustache(){
        Controller c = new Controller();
        ModelAndView test = c.accounts();
        MockRouter router = new MockRouter(new App());
        router.get("/account/moustache", rsp -> {
            assertEquals(test, rsp.value());
            assertEquals(StatusCode.OK, rsp.getStatusCode());
        });
    } */
}
