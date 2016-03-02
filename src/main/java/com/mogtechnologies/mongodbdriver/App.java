package com.mogtechnologies.mongodbdriver;

public class App {
    public static void main( String[] args ) {
        Thread thread = new Thread(new ServerService());
        thread.start();
    }
}
