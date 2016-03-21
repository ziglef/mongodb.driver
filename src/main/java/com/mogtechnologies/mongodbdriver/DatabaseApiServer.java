package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.controllers.EntryPoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

// TODO: Should make this singleton
public class DatabaseApiServer implements Runnable {
    public void run() {
        Server jettyServer = new Server(8081);

        ContextHandlerCollection handlers = new ContextHandlerCollection();
        jettyServer.setHandler(handlers);

        ServletContextHandler app = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);

        // Jersey servlet
        ServletHolder jerseyServlet = new ServletHolder(org.glassfish.jersey.servlet.ServletContainer.class);
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", EntryPoint.class.getCanonicalName());
        app.addServlet(jerseyServlet, "/jersey/*");

        try {
            // Run the mongo server
            // System.out.println("Working Directory = " + System.getProperty("user.dir"));
            Runtime.getRuntime().exec("cmd /c startMongo.bat", null, new File("src/main/resources/sh/"));

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
