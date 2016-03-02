package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.controllers.EntryPoint;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

// TODO: Should make this singleton
public class ServerService implements Runnable {
    public void run() {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        Server jettyServer = new Server(8081);
        jettyServer.setHandler(contextHandler);

        // Jersey servlet
        ServletHolder jerseyServlet = contextHandler.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tell the Jersey servlet what class/service to use as entry point
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                EntryPoint.class.getCanonicalName());

        try {
            // Run the mongo server
            // System.out.println("Working Directory = " + System.getProperty("user.dir"));
            Runtime.getRuntime().exec("cmd /c startMongo.bat", null, new File("src/main/resources/sh/"));

            // Run jetty server
            jettyServer.start();
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
