package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.database.DatabaseController;
import com.mogtechnologies.mongodbdriver.database.models.SimpleDocument;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import org.bson.*;
import org.mongodb.morphia.mapping.MappingException;
import org.mongodb.morphia.query.UpdateException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/* CURRENTLY DOING:
    ->  Adapt entrypoint to use DatabaseController;
    ->  Set path for POST requests to add documents
*/

// TODO: REMOVE ERROR HANDLING FROM POST METHOD
@Path("/object")
public class EntryPoint {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getObject(@PathParam("id") String id){
        Document whereQuery = new Document();
        whereQuery.put("id", new BsonString(id));

        Document document = DatabaseController
                                    .getInstance()
                                    .findOne("objects", whereQuery);

        return JSON.serialize(document);
    }

    @POST
    @Path("addobject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String putObject(SimpleDocument sd){
        try {
            DatabaseController.getInstance().getDatastore().save(sd);
        } catch (UpdateException e) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("400", "Bad Request");
            messageFields.put("ErrorCode", "400");
            messageFields.put("ExtraInfo", "The document can't be empty.");
            return JSON.serialize(messageFields);
        } catch (MappingException e) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("400", "Bad Request");
            messageFields.put("ErrorCode", "400");
            messageFields.put("ExtraInfo", "The document couldn't be mapped. Check your document structure.");
            return JSON.serialize(messageFields);
        } catch (Exception e) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("500", "Internal Server Error");
            messageFields.put("ErrorCode", "500");
            messageFields.put("ExtraInfo", "The server failed fulfilling your request. This could be either an error" +
                    " on our side or in your request. Please check your request and try again. If the error persists" +
                    " please contact an administrator.");
            return JSON.serialize(messageFields);
        }

        Map<String, String> messageFields = new HashMap<String, String>();
        messageFields.put("201", "Created");
        messageFields.put("ErrorCode", "201");
        messageFields.put("ExtraInfo", "The document was successfully created.");
        return JSON.serialize(messageFields);
    }

}
