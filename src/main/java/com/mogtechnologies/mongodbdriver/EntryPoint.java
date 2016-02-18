package com.mogtechnologies.mongodbdriver;

import com.github.fakemongo.Fongo;
import com.mongodb.*;
import com.mongodb.util.JSON;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/object")
public class EntryPoint {

    Fongo fongo = new Fongo("Fongo Server");
    DB db = fongo.getDB("testdb");
    DBCollection dbCollection = db.getCollection("testCollection");

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getObject(@PathParam("id") String id){
        dbCollection.insert(new BasicDBObject("id", "2"));

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("id", id);
        DBObject cursor = dbCollection.findOne(whereQuery);

        return JSON.serialize(cursor);
    }

}
