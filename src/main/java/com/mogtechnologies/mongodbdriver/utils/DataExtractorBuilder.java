package com.mogtechnologies.mongodbdriver.utils;


import java.util.ArrayList;
import java.util.Collections;

public class DataExtractorBuilder {

    private ArrayList<String> fieldNames;
    private ArrayList<ArrayList<ArrayList<String>>> dataParams;

    public DataExtractorBuilder( String fieldName ){
        this.fieldNames = new ArrayList<String>();
        this.fieldNames.add( fieldName );

        this.dataParams = new ArrayList<ArrayList<ArrayList<String>>>();
        this.dataParams.add( new ArrayList<ArrayList<String>>() );
    }

    public DataExtractorBuilder( ArrayList<String> fieldNames ){
        this.fieldNames = fieldNames;

        this.dataParams = new ArrayList<ArrayList<ArrayList<String>>>();
        for (int i = 0; i < fieldNames.size(); i++) {
            this.dataParams.add( new ArrayList<ArrayList<String>>() );
        }
    }

    public DataExtractorBuilder() {
        this.fieldNames = new ArrayList<String>();
        this.dataParams = new ArrayList<ArrayList<ArrayList<String>>>();
    }

    public void addParameterData( String fieldName, String parameters ) {
        int index = this.fieldNames.indexOf(fieldName);

        if (index == -1) {
            System.out.println("ERROR ADDING PARAMETERS TO DATA EXTRACTOR");
            return;
        }

        ArrayList<String> parametersList = new ArrayList<String>();
        String[] tokens = parameters.split("[.]");

        Collections.addAll(parametersList, tokens);

        this.dataParams.get(index).add(parametersList);
    }

    public ArrayList<String> getFieldNames() {
        return this.fieldNames;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getDataParams() {
        return this.dataParams;
    }

    public void addFieldName(String fieldName){
        this.fieldNames.add(fieldName);
        this.dataParams.add(new ArrayList<ArrayList<String>>());
    }
}
