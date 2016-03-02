package com.mogtechnologies.mongodbdriver.utils;

import com.mongodb.util.JSON;
import org.mongodb.morphia.mapping.MappingException;
import org.mongodb.morphia.query.UpdateException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class HttpResponseHandling {
    public static Response handleException(Exception e) {
        if (e.getClass() == NotFoundException.class) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("404", "Not Found");
            messageFields.put("Status Code", "404");
            messageFields.put("ExtraInfo", "The document couldn't be found.");
            return Response.status(Response.Status.NOT_FOUND).entity(JSON.serialize(messageFields)).build();
        } else if (e.getClass() == UpdateException.class) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("400", "Bad Request");
            messageFields.put("Status Code", "400");
            messageFields.put("ExtraInfo", "The document can't be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(JSON.serialize(messageFields)).build();
        } else if (e.getClass() == MappingException.class) {
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("400", "Bad Request");
            messageFields.put("Status Code", "400");
            messageFields.put("ExtraInfo", "The document couldn't be mapped. Check your document structure.");
            return Response.status(Response.Status.BAD_REQUEST).entity(JSON.serialize(messageFields)).build();
        } else {    // e.getClass() == Exception.class
            Map<String, String> messageFields = new HashMap<String, String>();
            messageFields.put("500", "Internal Server Error");
            messageFields.put("Status Code", "500");
            messageFields.put("ExtraInfo", "The server failed fulfilling your request. This could be either an error" +
                    " on our side or in your request. Please check your request and try again. If the error persists" +
                    " please contact an administrator.");

            /*
            messageFields.put("ExceptionType", e.getClass().getCanonicalName());
            messageFields.put("ExceptionMessage", e.getMessage());
            */
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JSON.serialize(messageFields)).build();
        }
    }

    public static Response handleSuccess(String httpCode, String extraInfo) {
        Map<String, String> messageFields = new HashMap<String, String>();

        Response.Status responseStatus = Response.Status.NOT_ACCEPTABLE;

        if ( httpCode.equals("200") ) {
            messageFields.put(httpCode, "Ok");
            responseStatus = Response.Status.OK;
        } else if ( httpCode.equals("201") ) {
            messageFields.put(httpCode, "Created");
            responseStatus = Response.Status.CREATED;
        } else if( httpCode.equals("202") ) {
            messageFields.put(httpCode,"Accepted");
            responseStatus = Response.Status.ACCEPTED;
        } else if( httpCode.equals("204") ) {
            messageFields.put(httpCode, "No Content");
            responseStatus = Response.Status.NO_CONTENT;
        } else if( httpCode.equals("205") ) {
            messageFields.put(httpCode, "Reset Content");
            responseStatus = Response.Status.RESET_CONTENT;
        } else if( httpCode.equals("206") ) {
            messageFields.put(httpCode, "Partial Content");
            responseStatus = Response.Status.PARTIAL_CONTENT;
        }

        messageFields.put("Status Code", httpCode);
        messageFields.put("ExtraInfo", extraInfo);
        return Response.status(responseStatus).entity(JSON.serialize(messageFields)).build();
    }
}
