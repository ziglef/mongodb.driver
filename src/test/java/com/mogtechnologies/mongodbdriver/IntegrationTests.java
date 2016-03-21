package com.mogtechnologies.mongodbdriver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mogtechnologies.mongodbdriver.controllers.DatabaseController;
import com.mogtechnologies.mongodbdriver.models.SimpleDocument;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.*;
import org.mongodb.morphia.mapping.MappingException;

import java.io.IOException;
import java.io.InputStreamReader;

import static com.mogtechnologies.mongodbdriver.Utils.createBasicJsonObject;
import static com.mogtechnologies.mongodbdriver.Utils.httpGet;
import static com.mogtechnologies.mongodbdriver.Utils.httpPost;

public class IntegrationTests {

    public static Thread thread;

    @BeforeClass
    public static void setUp(){
        // Startup our application
        thread = new Thread(new DatabaseApiServer());
        thread.start();
        // Add some documents to the database
        while( !thread.isAlive() ) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public IntegrationTests() {
        // Insert 10 documents on the database
        for (int i = 0; i < 10; i++) {
            ObjectNode jsonObject = createBasicJsonObject();

            SimpleDocument sd = new SimpleDocument(jsonObject);
            if (sd.getId() == null) {
                // Calling garbage collector
                sd = null;
                throw new MappingException("Error mapping json to simpleDocument");
            } else
                DatabaseController.getInstance().getDatastore().save(sd);
        }
    }

    @Test
    // GET Object | /object/{id}
    public void testGetObject() {
        int id = 0;
        CloseableHttpResponse response = httpGet("http://127.0.0.1:8081/jersey/object/" + id);

        if( response == null )
            Assert.assertFalse(true);
        else {
            JsonParser jp = new JsonParser();
            JsonElement root = null;
            try {
                root = jp.parse(new InputStreamReader(response.getEntity().getContent()));
                JsonObject rootObj = root.getAsJsonObject();
                Assert.assertEquals(id, rootObj.get("id").getAsInt());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    // POST Object | /object/addobject
    public void testPostObject() {
        ObjectNode jsonObject = createBasicJsonObject();
        CloseableHttpResponse response = httpPost("http://127.0.0.1:8081/jersey/object/addobject", jsonObject);

        if( response == null )
            Assert.assertFalse(true);
        else {
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());
        }
    }

    @AfterClass
    public static void tearDown(){
        thread.interrupt();
    }
}
