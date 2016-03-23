package com.mogtechnologies.mongodbdriver;


import com.mogtechnologies.mongodbdriver.utils.DataExtractor;
import com.mogtechnologies.mongodbdriver.utils.DataExtractorBuilder;

public class App {
    public static void main( String[] args ) {
        Thread databaseApiServer = new Thread(new DatabaseApiServer());
        databaseApiServer.start();

        Thread websocketsServer = new Thread(new WebSocketsServer());
        websocketsServer.start();
/*
        String customMetadata = "Assets.Document.Metadata.Metadata.CustomMetadata";
        String duration = "Assets.Document.Metadata.Metadata.Duration";

        DataExtractorBuilder deb = new DataExtractorBuilder();

        deb.addFieldName(duration);
        deb.addParameterData(duration, "Document");
        deb.addParameterData(duration, "Document");
        deb.addParameterData(duration, "Array.last");
        deb.addParameterData(duration, "Document");
        deb.addParameterData(duration, "Document");

        deb.addFieldName(customMetadata);
        deb.addParameterData(customMetadata, "Document");
        deb.addParameterData(customMetadata, "Document");
        deb.addParameterData(customMetadata, "Array.last");
        deb.addParameterData(customMetadata, "Document");
        deb.addParameterData(customMetadata, "Array.all");

        Thread dataExtractor = new Thread(new DataExtractor(deb.getFieldNames(), deb.getDataParams()));
        dataExtractor.start();
*/
    }
}
