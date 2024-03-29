package com.mogtechnologies.mongodbdriver;

import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.mogtechnologies.mongodbdriver.controllers.DataExtractorSocket;
import org.eclipse.jetty.util.component.LifeCycle;

public class EventClient
{
    public static void main(String[] args)
    {
        URI uri = URI.create("ws://localhost:8082/events/");

        try
        {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            try
            {
                // Attempt Connect
                Session session = container.connectToServer(DataExtractorSocket.class,uri);
                // Send a message
                session.getBasicRemote().sendText("Hello");
                // Close session
                session.close();
            }
            finally
            {
                // Force lifecycle stop when done with container.
                // This is to free up threads and resources that the
                // JSR-356 container allocates. But unfortunately
                // the JSR-356 spec does not handle lifecycles (yet)
                if (container instanceof LifeCycle)
                {
                    ((LifeCycle)container).stop();
                }
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}