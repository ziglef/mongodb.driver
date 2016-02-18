package com.mogtechnologies.mongodbdriver;

// JUnit imports
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

// Fongo
import com.github.fakemongo.Fongo;

// Mongo
import com.mongodb.DB;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        /* INSERT AND SEARCH FONGO EXAMPLE
        // Create a new fongo instance (fake mongo)
        Fongo fongo = new Fongo("Mongo Server | testApp");

        DB db = fongo.getDB("appdb");
        DBCollection dbCollection = db.getCollection("testCollection");
        dbCollection.insert(new BasicDBObject("name", "jon"));
        dbCollection.insert(new BasicDBObject("name", "jon"));

        assertEquals(db.getCollection("testCollection").getCount(), 2);
        */

        try {
            int id = 2;
            String urlString = "http://127.0.0.1:8080/object/" + id;
            URL url = new URL(urlString);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject();

            assertEquals(rootobj.get("id").getAsInt(), id);
        } catch (MalformedURLException e) {
            System.out.println("Error building URL.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error opening connection to host.");
            e.printStackTrace();
        }
    }
}
