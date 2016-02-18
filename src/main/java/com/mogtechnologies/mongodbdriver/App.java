package com.mogtechnologies.mongodbdriver;

// Jetty
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/*
 * Simple Jetty server
 */
public class App {
    public static void main( String[] args ) {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(contextHandler);

        ServletHolder jerseyServlet = contextHandler.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tell the Jersey servlet what class/service to use as entry point
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                EntryPoint.class.getCanonicalName());

        try {
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
