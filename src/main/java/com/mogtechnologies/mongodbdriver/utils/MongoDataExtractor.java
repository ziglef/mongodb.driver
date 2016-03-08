package com.mogtechnologies.mongodbdriver.utils;

import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

// MongoDataExtractor
public class MongoDataExtractor implements Runnable{

    //////////////////////////////////////////////
    //      DEFINITION AND INITIALIZATION       //
    //////////////////////////////////////////////

    MongoDatabase mongoDatabase;
    MongoCollection<Document> collection;
    String collectionName;

    // Constructor
    public MongoDataExtractor(String collectionName) {
        // Initialize DB connection
        String dbUrl = "192.168.1.131";
        int dbPort = 27017;
        String dbName = "LogData";

        MongoClient mongoClient = new MongoClient(dbUrl, dbPort);
        this.collectionName = collectionName;
        this.mongoDatabase = mongoClient.getDatabase( dbName );
        this.collection = mongoDatabase.getCollection("InfoLog_cap");
    }

    public void run() {
        Bson fieldsToScan = Projections.include("Assets.Document.Metadata.Metadata.CustomMetadata");
        MongoCursor<Document> cursor = collection.find().projection(fieldsToScan).cursorType(CursorType.TailableAwait).iterator();

        while(true){
            while(cursor.hasNext()){
                try {
                    BsonDocument doc = BsonDocument.parse(cursor.next().get("Assets", Document.class).toJson());
                    System.out.println("\nBsonDocument: " + doc.getDocument("Document").getArray("Metadata").get(0).asDocument().getDocument("Metadata").getArray("CustomMetadata"));
                } catch (NullPointerException e) {
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Don't interrupt me while i am sleeping!");
                e.printStackTrace();
            }
        }
    }

}

/*
Support
"String" "Array" "String" "String"

["Document"] ["Metadata", 0]
 */