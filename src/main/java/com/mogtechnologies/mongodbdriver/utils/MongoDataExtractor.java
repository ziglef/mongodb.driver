package com.mogtechnologies.mongodbdriver.utils;

import com.mongodb.CursorType;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import org.bson.*;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Map;

// MongoDataExtractor
public class MongoDataExtractor implements Runnable{

    //////////////////////////////////////////////
    //      DEFINITION AND INITIALIZATION       //
    //////////////////////////////////////////////

    MongoDatabase mongoDatabase;
    MongoCollection<Document> collection;
    ArrayList<String> dataNames;
    ArrayList<ArrayList<ArrayList<String>>> dataParameters;

    // Constructor
    public MongoDataExtractor(ArrayList<String> dataNames,
                              ArrayList<ArrayList<ArrayList<String>>> dataParameters) {
        // Initialize DB connection
        String dbUrl = "192.168.1.131";
        int dbPort = 27017;
        String dbName = "LogData";

        MongoClient mongoClient = new MongoClient(dbUrl, dbPort);
        this.mongoDatabase = mongoClient.getDatabase( dbName );
        this.collection = mongoDatabase.getCollection("InfoLog_cap");
        this.dataNames = dataNames;
        this.dataParameters = dataParameters;
    }

    public void run() {
/*
        ArrayList<String> fieldNames = new ArrayList<String>();
        int curr = 0;

        for( ArrayList<String> fieldString : this.dataNames ){
            fieldNames.add(curr, collectionName);
            String tmpString = "";
            for( String s : fieldString ){
                tmpString = tmpString.concat(s);
            }
            tmpString = fieldNames.get(curr).concat(tmpString);
            fieldNames.set(curr, tmpString);
            curr++;
        }
*/
        Bson fieldsToScan = Projections.include(dataNames);
        // Bson fieldsToScan = Projections.include("Assets.Document.Metadata.Metadata.CustomMetadata");
        MongoCursor<Document> cursor = collection
                                            .find()
                                            .projection(fieldsToScan)
                                            .cursorType(CursorType.TailableAwait)
                                            .iterator();

        while(true){
            while(cursor.hasNext()){
                try {
                    int currField = 0;
                    int currData = 0;

                    BsonDocument doc = BsonDocument.parse(cursor.next().toJson());
                    System.out.println("\nOriginal Document: " + doc);
                    System.out.println("What we should see: " +
                            doc
                                    .getDocument("Assets")
                                    .getDocument("Document")
                                    .getArray("Metadata").get(0).asDocument()
                                    .getDocument("Metadata")
                                    .getArray("CustomMetadata").toString());
                    for( String dataName : dataNames ){
                        BsonDocument obtainedDoc = doc;
                        String[] fieldNames = dataName.split("[.]");
                        ArrayList<ArrayList<String>> documentParameters = dataParameters.get(currData);

                        for( String fieldName : fieldNames ){
                            ArrayList<String> fieldParameters = documentParameters.get(currField);
                            if( fieldParameters.get(0).equals("Document") ) {
                                System.out.println("Getting " + fieldName + " as a Document.");
                                obtainedDoc = obtainedDoc
                                        .getDocument(fieldName);
                            } else if( fieldParameters.get(0).equals("Array") ) {
                                if( fieldParameters.get(1).equals("all") ) {
                                    System.out.println("Getting " + fieldName + " as an Array, as a Document.");
                                    BsonDocument tmpDoc = new BsonDocument();
                                    for( BsonValue be : obtainedDoc.getArray(fieldName) ){
                                        BsonDocument bd = (BsonDocument)be;

                                        BsonString tmpKey = new BsonString("");
                                        boolean indexHack = true;
                                        for( Map.Entry<String, BsonValue> bv : bd.entrySet() ){
                                            System.out.println("BV: " + bv.getValue());

                                            if( indexHack ) {
                                                tmpKey = bv.getValue().asString();
                                                indexHack = !indexHack;
                                            } else {
                                                tmpDoc = tmpDoc.append(tmpKey.getValue(), bv.getValue());
                                                indexHack = !indexHack;
                                            }
                                        }
                                    }
                                    obtainedDoc = tmpDoc;
                                    /*
                                    obtainedDoc = obtainedDoc
                                            .getArray(fieldName)
                                            .asDocument();
                                    */
                                } else if( Integer.valueOf(fieldParameters.get(1)) != null ) {
                                    System.out.println("Getting " + fieldName + " as an Array, with field " + Integer.valueOf(fieldParameters.get(1)) + " as a Document.");
                                    obtainedDoc = obtainedDoc
                                            .getArray(fieldName)
                                            .get(Integer.valueOf(fieldParameters.get(1)))
                                            .asDocument();
                                }
                            } else
                                break;

                            currField++;
                            System.out.println("Current Document: " + obtainedDoc);
                        }

                        currField = 0;
                        currData++;
                    }

                } catch (NullPointerException e) {
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                } catch (BsonInvalidOperationException e){
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                } catch (IndexOutOfBoundsException e){
                    // System.out.println("This Document is not an Asset");
                    // e.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000);
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