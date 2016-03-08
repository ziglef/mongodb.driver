package com.mogtechnologies.mongodbdriver;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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

    public static ObjectNode createBasicJsonObject() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("text", "sample text");
        node.put("title", "sample title");
        return node;
    }

    public static void clearCollection(DBCollection testCollection) {
        testCollection.drop();
    }

    public static CloseableHttpResponse httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            return httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static CloseableHttpResponse httpPost(String url, ObjectNode json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("content-type", "application/json");
        try {
            httpPost.setEntity(new StringEntity(json.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            return httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
