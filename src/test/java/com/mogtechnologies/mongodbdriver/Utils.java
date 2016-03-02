package com.mogtechnologies.mongodbdriver;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static void insertBasicObjectIntoDB(DBCollection dbCollection) {
        BasicDBObject basicDBObject = createBasicDBObject();

        dbCollection.insert(basicDBObject);
    }

    public static BasicDBObject createBasicDBObject() {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("title", "sample title");
        basicDBObject.put("text", "sample text");
        basicDBObject.put("id", 0);

        return basicDBObject;
    }

    // TODO: Create the body of the method
    public static ObjectNode createBasicJsonObject() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("text", "sample text");
        node.put("title", "sample title");
        return node;
    }

    public static void clearCollection(DBCollection testCollection) {
        testCollection.drop();
    }

    public static InputStream httpGet(String url) {
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
