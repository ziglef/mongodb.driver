package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.database.DatabaseController;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import org.bson.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/* CURRENTLY DOING:
    ->  Adapt entrypoint to use DatabaseController;
    ->  Set path for POST requests to add documents
*/

// TODO: Add POST method to add objects to the database
@Path("/object")
public class EntryPoint {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getObject(@PathParam("id") String id){
        Document whereQuery = new Document();
        whereQuery.put("id", new BsonString(id));

        FindIterable cursor = DatabaseController
                                    .getInstance()
                                    .find("objects", whereQuery);

        return JSON.serialize(cursor);
    }

    @POST
    @Path("addobject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String putObject(TestObject to){
        return "";
        // DatabaseController.getInstance().addDocument("objects", to);
    }

}
