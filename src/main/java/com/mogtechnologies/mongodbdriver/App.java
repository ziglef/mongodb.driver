package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.utils.MongoDataExtractor;

public class App {
    public static void main( String[] args ) {
        Thread server = new Thread(new ServerService());
        server.start();

        Thread dataExtractor = new Thread(new MongoDataExtractor("Assets"));
        dataExtractor.start();
    }
}
