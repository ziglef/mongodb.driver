package com.mogtechnologies.mongodbdriver;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mogtechnologies.mongodbdriver.controllers.EntryPoint;
import com.mogtechnologies.mongodbdriver.utils.HttpResponseHandling;
import com.mongodb.util.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mongodb.morphia.mapping.MappingException;

import javax.ws.rs.core.Response;

import static com.mogtechnologies.mongodbdriver.Utils.createBasicDBObject;
import static com.mogtechnologies.mongodbdriver.Utils.createBasicJsonObject;
import static org.mockito.Mockito.when;


// TODO: Reorder node creation
public class RestApiTests {

    EntryPoint entryPointMock;

    public RestApiTests() {
        entryPointMock = Mockito.mock(EntryPoint.class);

        // Create Mocked values for valid and invalid calls
        // getObject(String id)
        when(entryPointMock.getObject("0"))
                .thenReturn(Response.status(Response.Status.OK)
                        .entity(JSON.serialize(createBasicDBObject()))
                        .build());

        when(entryPointMock.getObject("-1"))
                .thenReturn(HttpResponseHandling
                        .handleException(new NullPointerException()));


        // putObject(JsonNode jsonObject)
        when(entryPointMock.putObject(null))
                .thenReturn(HttpResponseHandling
                        .handleException(new NullPointerException()));

        ObjectNode jsonObject = createBasicJsonObject();
        jsonObject.remove("title");
        when(entryPointMock.putObject(jsonObject))    // Should be badly formed document here
                .thenReturn(HttpResponseHandling
                        .handleException(new MappingException("Error mapping json to simpleDocument")));

        jsonObject = createBasicJsonObject();
        when(entryPointMock.putObject(jsonObject))    // Should be correctly formed document here
                .thenReturn(HttpResponseHandling
                        .handleSuccess("201", "The document was successfully created."));
    }

    @Test
    // GET Object | /object/{id}
    public void testGetObject() {
        // .toString() was needed to trim garbage
        Assert.assertEquals(Response.status(Response.Status.OK)
                                .entity(JSON.serialize(createBasicDBObject()))
                                .build().toString(),
                            entryPointMock.getObject("0").toString());

        // .toString() was needed to trim garbage
        Assert.assertEquals(HttpResponseHandling
                                .handleException(new NullPointerException()).toString(),
                            entryPointMock.getObject("-1").toString());
    }

    @Test
    // POST Object | /object/addobject
    public void testPostObject() {
        ObjectNode jsonObject = createBasicJsonObject();
        jsonObject.remove("title");

        // .toString() was needed to trim garbage
        Assert.assertEquals(HttpResponseHandling
                                .handleException(new MappingException("Error mapping json to simpleDocument"))
                                .toString(),
                entryPointMock.putObject(jsonObject).toString());

        jsonObject = createBasicJsonObject();
        // .toString() was needed to trim garbage
        Assert.assertEquals(HttpResponseHandling
                                .handleSuccess("201", "The document was successfully created.")
                                .toString(),
                entryPointMock.putObject(jsonObject).toString());
    }
}
