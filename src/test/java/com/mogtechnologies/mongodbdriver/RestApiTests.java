package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.controllers.EntryPoint;
import com.mogtechnologies.mongodbdriver.utils.HttpResponseHandling;
import com.mongodb.util.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static com.mogtechnologies.mongodbdriver.Utils.createBasicDBObject;
import static org.mockito.Mockito.when;

public class RestApiTests {

    EntryPoint entryPointMock;

    public RestApiTests() {
        entryPointMock = Mockito.mock(EntryPoint.class);

        // Create Mocked values for valid and invalid calls
        when(entryPointMock.getObject("0"))
                .thenReturn(Response.status(Response.Status.OK)
                        .entity(JSON.serialize(createBasicDBObject()))
                        .build());

        when(entryPointMock.getObject("-1"))
                .thenReturn(HttpResponseHandling
                        .handleException(new NullPointerException()));
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


    }
}
