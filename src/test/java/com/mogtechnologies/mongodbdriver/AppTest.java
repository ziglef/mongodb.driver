package com.mogtechnologies.mongodbdriver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.DBObject;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.bson.Document;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppTest extends TestCase {

    Fongo fongo;
    DB testDB;
    DBCollection testCollection;

    public AppTest( String testName ) {
        super( testName );
        this.fongo = new Fongo("Fongo Mongo"); // Fake mongoDB
        this.testDB = fongo.getDB("testDB");
        this.testCollection = testDB.getCollection("testCollection");
    }

    public static Test suite() {
        return new TestSuite( AppTest.class );
    }

    // DB Tests
    public void testDB(){
        testDBInsert();
        testDBQuery();
    }

    public void testDBInsert() {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("title", "sample title");
        basicDBObject.put("text", "sample text");
        basicDBObject.put("id", 0);

        testCollection.insert(basicDBObject);

        assertEquals(testDB.getCollection("testCollection").getCount(), 1);
    }

    public void testDBQuery() {
        DBObject queryResults = testCollection.find().one();

        assertEquals((Integer)queryResults.get("id"), new Integer(0));
    }

    // REST API Tests
    public void testRestAPI(){
        // EntryPoint
        testEntryPoint();
        // Add more APIs here
    }

    // Integration tests | Every test but now without mocks
    public void testIntegration(){
        // testDBIntegration();
        // testRestAPIIntegration();
    }
    // TODO: use fongo to test insertion and search
    // TODO: use mockito to test RESTful API
    public void testApp() {
        testDB();
        testRestAPI();
        testIntegration();
    }

    public void testEntryPoint() {
        // POST Object | /object/addobject

        // GET Object | /object/{id}
        int id = 0;
        InputStream response = httpGet("http://127.0.0.1:8080/object/" + id);

        if( response == null )
            assertFalse(true);
        else {
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader(response));
            JsonObject rootObj = root.getAsJsonObject();

            assertEquals(rootObj.get("id").getAsInt(), id);
        }
    }

    private InputStream httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

/*
        try {
            int id = 2;
            String urlString = "http://127.0.0.1:8080/object/" + id;
            URL url = new URL(urlString);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootObj = root.getAsJsonObject();

            assertEquals(rootObj.get("id").getAsInt(), id);
        } catch (MalformedURLException e) {
            System.out.println("Error building URL.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error opening connection to host.");
            e.printStackTrace();
        }
 */