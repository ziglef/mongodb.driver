package com.mogtechnologies.mongodbdriver.database;

import javax.inject.Singleton;

// Singleton Pattern Database Controller
public class DatabaseController {

    // Constructor
    private DatabaseController() {
        /* Do Stuff Here */
    }

    // Access Point | Thread safe
    public static DatabaseController getInstance(){
        return DatabaseControllerHolder.INSTANCE;
    }

    // Instance of the singleton
    private static class DatabaseControllerHolder{
        private static final DatabaseController INSTANCE = new DatabaseController();
    }
}
