package com.mogtechnologies.mongodbdriver;

import com.github.fakemongo.Fongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.*;

import static com.mogtechnologies.mongodbdriver.Utils.clearCollection;
import static com.mogtechnologies.mongodbdriver.Utils.insertBasicObjectIntoDB;


public class DatabaseTests{
    private DBCollection testCollection;

    public DatabaseTests() {
        Fongo fongo = new Fongo("Fongo Mongo");
        DB testDB = fongo.getDB("testDB");
        this.testCollection = testDB.getCollection("testCollection");
    }

    @Test
    public void testDBInsert() {
        clearCollection(this.testCollection);
        insertBasicObjectIntoDB(this.testCollection);

        Assert.assertEquals(1, this.testCollection.getCount());
    }

    @Test
    public void testDBQuery() {
        clearCollection(this.testCollection);
        insertBasicObjectIntoDB(this.testCollection);

        DBObject queryResults = this.testCollection.find().one();

        Assert.assertEquals(0, queryResults.get("id"));
    }
}
