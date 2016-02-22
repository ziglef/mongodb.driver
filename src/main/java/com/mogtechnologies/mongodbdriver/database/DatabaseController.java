package com.mogtechnologies.mongodbdriver.database;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonString;
import org.bson.Document;

// Singleton Pattern Database Controller
public class DatabaseController {

    //////////////////////////////////////////////
    //      DEFINITION AND INITIALIZATION       //
    //////////////////////////////////////////////

    private static String dbUrl;
    private static Integer dbPort;
    private static String dbName;
    private static String dbUser;
    private static String dbPassword;

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;
    /*
    private static ArrayList<MongoCollection> mongoCollections;
    private static final ArrayList<String> mongoCollectionsNames = new ArrayList<String>();
    */
    // Constructor
    private DatabaseController() {
        // Initialize DB connection
        dbUrl = "localhost";
        dbPort = 27017;
        dbName = "mogDB";
        dbUser = "";
        dbPassword = "";

        mongoClient = new MongoClient( dbUrl, dbPort );
        mongoClient.setWriteConcern( WriteConcern.JOURNALED );
        mongoDatabase = mongoClient.getDatabase( dbName );

        // TODO: Remove initial drop and initialization.
        mongoDatabase.drop();
        if( mongoDatabase.getCollection("objects") == null ) mongoDatabase.createCollection("objects");

        for(int i=0; i<10; i++) {
            Document document = new Document();
            document.put("id", new BsonString(String.valueOf(i)));
            this.addDocument("objects", document);
        }

        /* Just here in case we need something similar

        mongoCollections = new ArrayList<MongoCollection>( mongoCollectionsNames.size() );
        for( String s : mongoCollectionsNames ){
            mongoCollections.add( mongoDatabase.getCollection(s) );
        }
        */
    }

    // Access Point | Thread safe
    public static DatabaseController getInstance(){
        return DatabaseControllerHolder.INSTANCE;
    }

    // Instance of the singleton
    private static class DatabaseControllerHolder{
        private static final DatabaseController INSTANCE = new DatabaseController();
    }

    //////////////////////////////
    //      CLASS METHODS       //
    //////////////////////////////

    // GETTERS
    public MongoClient getClient(){ return this.mongoClient; }
    public MongoDatabase getDatabase(){ return this.mongoDatabase; }
    public MongoCollection<Document> getCollection( String s ){ return this.mongoDatabase.getCollection(s); }

    // ADDERS
    public void addDocument(String collectionName, Document document){
        mongoDatabase.getCollection(collectionName).insertOne(document);
    }

    // FINDERS (KEEPERS)
    // Here we should let the user get the collection they want and query it
    // I'll just leave this here if a future need rises
    public FindIterable find(String collectionName, Document properties){
        return mongoDatabase.getCollection(collectionName).find(properties);
    }
}