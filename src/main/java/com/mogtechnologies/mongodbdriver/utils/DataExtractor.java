package com.mogtechnologies.mongodbdriver.utils;

import com.mogtechnologies.mongodbdriver.controllers.DatabaseController;
import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.*;
import org.bson.conversions.Bson;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

// DataExtractor
public class DataExtractor implements Runnable{

    //////////////////////////////////////////////
    //      DEFINITION AND INITIALIZATION       //
    //////////////////////////////////////////////

    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> collection;
    private ArrayList<String> dataNames;
    private ArrayList<ArrayList<ArrayList<String>>> dataParameters;
    private long id;

    private Session session = null;

    // Constructor
    public DataExtractor(ArrayList<String> dataNames,
                         ArrayList<ArrayList<ArrayList<String>>> dataParameters, Session session) {
        // Initialize DB connection
        String dbUrl = "192.168.1.131";
        int dbPort = 27017;
        String dbName = "LogData";

        MongoClient mongoClient = new MongoClient(dbUrl, dbPort);
        this.mongoDatabase = mongoClient.getDatabase( dbName );
        this.collection = mongoDatabase.getCollection("InfoLog_cap");
        this.dataNames = new ArrayList<String>(dataNames);
        this.dataParameters = new ArrayList<ArrayList<ArrayList<String>>>(dataParameters);
        this.id = 0;

        this.session = session;
    }

    public DataExtractor(ArrayList<String> dataNames,
                         ArrayList<ArrayList<ArrayList<String>>> dataParameters) {
        // Initialize DB connection
        String dbUrl = "192.168.1.131";
        int dbPort = 27017;
        String dbName = "LogData";

        MongoClient mongoClient = new MongoClient(dbUrl, dbPort);
        this.mongoDatabase = mongoClient.getDatabase(dbName);
        this.collection = mongoDatabase.getCollection("InfoLog_cap");
        this.dataNames = new ArrayList<String>(dataNames);
        this.dataParameters = new ArrayList<ArrayList<ArrayList<String>>>(dataParameters);
        this.id = 0;
    }

    public void run() {
        Bson fieldsToScan = Projections.include(dataNames);

        System.out.println("\n\n\n" + dataNames.toString() + "\n\n\n");

        MongoCursor<Document> cursor = collection
                .find()
                .projection(fieldsToScan)
                .cursorType(CursorType.TailableAwait)
                .iterator();

        while(true){
            while (!Thread.interrupted() && cursor.hasNext()) {
                try {
                    int currField = 0;
                    int currData = 0;

                    BsonDocument doc = BsonDocument.parse(cursor.next().toJson());
                    BsonDocument finalDoc = new BsonDocument();

                    // DEBUG //
                    System.out.println("\nOriginal Document: " + doc);
                    System.out.println("What we should see: " +
                            doc
                                    .getDocument("Assets")
                                    .getDocument("Document")
                                    .getArray("Metadata").get(0).asDocument()
                                    .getDocument("Metadata")
                                    .getArray("CustomMetadata").toString());
                    // DEBUG //

                    for (String dataName : dataNames) {

                        BsonDocument obtainedDoc = doc;
                        BsonValue obtainedField = null;
                        String[] fieldNames = dataName.split("[.]");
                        ArrayList<ArrayList<String>> documentParameters = dataParameters.get(currData);

                        // DEBUG //
                        System.out.println("Trying to find: " + dataName);
                        // DEBUG //

                        for (String fieldName : fieldNames) {

                            ArrayList<String> fieldParameters = documentParameters.get(currField);
                            if (fieldParameters.get(0).equals("Document")) {

                                // DEBUG //
                                System.out.println("Getting " + fieldName + " as a Document.");
                                // DEBUG //

                                try {
                                    obtainedDoc = obtainedDoc
                                            .getDocument(fieldName);
                                } catch (Exception e) {
                                    obtainedDoc = null;
                                    break;
                                }

                            } else if (fieldParameters.get(0).equals("Array")) {

                                if (fieldParameters.get(1).equals("all")) {

                                    // DEBUG //
                                    System.out.println("Getting " + fieldName + " as an Array, as a Document.");
                                    // DEBUG //

                                    BsonDocument tmpDoc = new BsonDocument();
                                    for (BsonValue be : obtainedDoc.getArray(fieldName)) {

                                        BsonDocument bd = (BsonDocument) be;

                                        BsonString tmpKey = new BsonString("");
                                        boolean indexHack = true;

                                        for (Map.Entry<String, BsonValue> bv : bd.entrySet()) {

                                            // DEBUG //
                                            System.out.println("BV: " + bv.getValue());
                                            // DEBUG //

                                            if (indexHack) {
                                                tmpKey = bv.getValue().asString();
                                                indexHack = !indexHack;
                                            } else {
                                                tmpDoc = tmpDoc.append(tmpKey.getValue(), bv.getValue());
                                                indexHack = !indexHack;
                                            }
                                        }
                                    }

                                    if (tmpDoc != null)
                                        obtainedDoc = tmpDoc;
                                    else
                                        obtainedDoc = null;

                                } else if (fieldParameters.get(1).equals("last")) {

                                    // DEBUG //
                                    System.out.println("Getting " + fieldName + " as an Array, with field " + (obtainedDoc.getArray(fieldName).size() - 1) + " as a Document.");
                                    // DEBUG //

                                    obtainedDoc = obtainedDoc
                                            .getArray(fieldName)
                                            .get(obtainedDoc.getArray(fieldName).size() - 1)
                                            .asDocument();

                                } else if (Integer.valueOf(fieldParameters.get(1)) != null) {

                                    // DEBUG //
                                    System.out.println("Getting " + fieldName + " as an Array, with field " + Integer.valueOf(fieldParameters.get(1)) + " as a Document.");
                                    // DEBUG //

                                    obtainedDoc = obtainedDoc
                                            .getArray(fieldName)
                                            .get(Integer.valueOf(fieldParameters.get(1)))
                                            .asDocument();
                                }

                            } else if (fieldParameters.get(0).equals("Field")) {
                                // DEBUG //
                                System.out.println("Getting " + fieldName + " as a Field.");
                                // DEBUG //

                                obtainedField = obtainedDoc.get(fieldName);
                            } else
                                break;

                            currField++;
                            System.out.println("Current Document: " + obtainedDoc);
                        }

                        currField = 0;
                        currData++;

                        if (obtainedDoc != null) {
                            BsonDocument tempDoc = new BsonDocument();
                            if (obtainedField != null) {
                                tempDoc.append(fieldNames[fieldNames.length - 1], obtainedField);
                                finalDoc.putAll(tempDoc);
                            } else {
                                tempDoc.append(fieldNames[fieldNames.length - 1], obtainedDoc);
                                finalDoc.putAll(tempDoc);
                            }
                        }
                    }

                    System.out.println("Final Document: " + finalDoc);
                    finalDoc.put("id", new BsonInt64(this.id));
                    this.id++;

                    if (session == null) {
                        DatabaseController.getInstance().getCollection("log", BsonDocument.class).insertOne(finalDoc);
                    } else {
                        // Send document to the websocket
                        try {
                            session.getBasicRemote().sendText(finalDoc.toJson());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (NullPointerException e) {
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                } catch (BsonInvalidOperationException e) {
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                }
            }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
    }

}