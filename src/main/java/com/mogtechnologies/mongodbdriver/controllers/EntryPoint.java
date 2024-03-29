package com.mogtechnologies.mongodbdriver.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.mogtechnologies.mongodbdriver.models.SimpleDocument;
import com.mogtechnologies.mongodbdriver.utils.HttpResponseHandling;
import com.mongodb.util.JSON;
import org.bson.*;
import org.mongodb.morphia.mapping.MappingException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/object")
public class EntryPoint {

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getObject(@PathParam("id") String id){
        Document whereQuery = new Document();
        whereQuery.put("id", new BsonInt32(Integer.valueOf(id)));

        Document document = DatabaseController
                                    .getInstance()
                                    .findOne("simpledocuments", whereQuery);

        if( document != null )
            return Response.status(Response.Status.OK).entity(JSON.serialize(document)).build();
        else
            return HttpResponseHandling.handleException(new NotFoundException());
    }

    @POST
    @Path("/addobject")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putObject(JsonNode jsonObject){
        try {
            SimpleDocument sd = new SimpleDocument(jsonObject);

            if( sd.getId() == null ) {
                // Calling garbage collector
                sd = null;
                throw new MappingException("Error mapping json to simpleDocument");
            } else
                DatabaseController.getInstance().getDatastore().save(sd);
        } catch (Exception e){
            return HttpResponseHandling.handleException(e);
        }
        Document whereQuery = new Document();
        whereQuery.put("id", new BsonInt32(0));

        Document document = DatabaseController
                .getInstance()
                .findOne("simpledocuments", whereQuery);
        return Response.status(Response.Status.OK).entity(JSON.serialize(document)).build();
        //        return HttpResponseHandling.handleSuccess("201", "The document was successfully created.");
    }

}
