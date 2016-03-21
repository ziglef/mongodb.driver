package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.controllers.DataExtractorSocket;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import java.io.File;

// TODO: Should make this singleton
public class WebSocketsServer implements Runnable {

    public void run() {
        Server jettyServer = new Server(8082);

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        jettyServer.setHandler(handlers);

        ServletContextHandler app = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);

        try {
            // Initialize javax.websocket layer
            ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(app);
            wscontainer.addEndpoint(DataExtractorSocket.class);

            // Run jetty server
            jettyServer.start();
            // jettyServer.dump(System.err);
            jettyServer.join();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while starting jetty server.\n" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception while starting jetty server.\n" + e.getMessage());
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }
}
