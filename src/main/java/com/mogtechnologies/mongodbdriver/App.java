package com.mogtechnologies.mongodbdriver;

import com.mogtechnologies.mongodbdriver.utils.MongoDataExtractor;

import java.util.ArrayList;

public class App {
    public static void main( String[] args ) {
        Thread server = new Thread(new ServerService());
        server.start();

        ArrayList<String> fieldNames = new ArrayList<String>();
        fieldNames.add("Assets.Document.Metadata.Metadata.CustomMetadata");

        ArrayList<ArrayList<String>> fieldParams = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> dataParams = new ArrayList<ArrayList<ArrayList<String>>>();

        ArrayList<String> assetsParams = new ArrayList<String>();
        assetsParams.add("Document");
        ArrayList<String> documentsParams = new ArrayList<String>();
        documentsParams.add("Document");
        ArrayList<String> metadataParams = new ArrayList<String>();
        metadataParams.add("Array");
        metadataParams.add("0");
        ArrayList<String> metadataParams1 = new ArrayList<String>();
        metadataParams1.add("Document");
        ArrayList<String> customMetadataParams = new ArrayList<String>();
        customMetadataParams.add("Array");
        customMetadataParams.add("all");

        fieldParams.add(assetsParams);
        fieldParams.add(documentsParams);
        fieldParams.add(metadataParams);
        fieldParams.add(metadataParams1);
        fieldParams.add(customMetadataParams);

        dataParams.add(fieldParams);

        Thread dataExtractor = new Thread(new MongoDataExtractor(fieldNames, dataParams));
        dataExtractor.start();
    }
}
