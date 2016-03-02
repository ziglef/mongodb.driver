package com.mogtechnologies.mongodbdriver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

import static com.mogtechnologies.mongodbdriver.Utils.httpGet;

public class IntegrationTests {

    public IntegrationTests() {
        // Startup our application
        // Add some documents to the database
        // Run the tests
    }

    @Test
    // GET Object | /object/{id}
    public void testGetObject() {
        int id = 0;
        InputStream response = httpGet("http://127.0.0.1:8080/object/" + id);

        if( response == null )
            Assert.assertFalse(true);
        else {
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader(response));
            JsonObject rootObj = root.getAsJsonObject();

            Assert.assertEquals(rootObj.get("id").getAsInt(), id);
        }
    }

    @Test
    // POST Object | /object/addobject
    public void testPostObject() {


    }
}
