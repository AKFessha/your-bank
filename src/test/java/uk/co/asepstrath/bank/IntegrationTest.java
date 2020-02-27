package uk.co.asepstrath.bank;

import io.jooby.JoobyTest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JoobyTest(App.class)
public class IntegrationTest {
    static OkHttpClient client = new OkHttpClient();

    @Test
    public void testVFAHTTP200() {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/VFA")
                .build();
            try (Response rsp = client.newCall(req).execute()) {
                assertEquals(200,rsp.code());
            }catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Test
    public void testJson() throws JSONException {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/json")
                .build();
        try (InputStream resourceAsStream = UnitTest.class.getResourceAsStream("/sum.json")) {
            String json = IOUtils.toString(resourceAsStream, "utf-8");
            try (Response rsp = client.newCall(req).execute()) {
                JSONAssert.assertEquals(json,rsp.body().toString(),true);
            }

        } catch (IOException e) {
        }

    }

    @Test
    public void testAPI() throws  JSONException{
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/api")
                .build();
        try (InputStream resourceAsStream = UnitTest.class.getResourceAsStream("/apitest.json")) {
            String json = IOUtils.toString(resourceAsStream, "utf-8");
            try (Response rsp = client.newCall(req).execute()) {
                JSONAssert.assertEquals(json,rsp.body().toString(),true);
            }

        } catch (IOException e) {
        }
    }

    @Test
    public void testAccountPageHTTP200() {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/account")
                .build();
        try (Response rsp = client.newCall(req).execute()) {
            assertEquals(200,rsp.code());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTransactionPageHTTP200() {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/transaction")
                .build();
        try (Response rsp = client.newCall(req).execute()) {
            assertEquals(200,rsp.code());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHomePageHTTP200() {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/")
                .build();
        try (Response rsp = client.newCall(req).execute()) {
            assertEquals(200,rsp.code());
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonTrans() throws JSONException {
        Request req = new Request.Builder()
                .url("http://localhost:" + "8080"+"/jsonTRANS")
                .build();
        try (InputStream resourceAsStream = UnitTest.class.getResourceAsStream("/trans.json")) {
            String json = IOUtils.toString(resourceAsStream, "utf-8");
            try (Response rsp = client.newCall(req).execute()) {
                JSONAssert.assertEquals(json,rsp.body().toString(),true);
            }

        } catch (IOException e) {
        }

    }

}
