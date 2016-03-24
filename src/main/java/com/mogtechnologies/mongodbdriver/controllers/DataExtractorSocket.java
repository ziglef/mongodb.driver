package com.mogtechnologies.mongodbdriver.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mogtechnologies.mongodbdriver.utils.DataExtractor;
import com.mogtechnologies.mongodbdriver.utils.DataExtractorBuilder;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class DataExtractorSocket {

    private Session session = null;
    private Thread dataExtractorThread = null;

    @OnOpen
    public void onWebSocketConnect(Session sess)
    {
        System.out.println("Socket Connected: " + sess);
        if( this.session == null )
            this.session = sess;
    }

    @OnMessage
    public void onWebSocketText(String message) throws IOException {
        System.out.println("Received TEXT message: " + message);
        JsonObject json = new JsonParser().parse(message).getAsJsonObject();

        if( json.get("action").getAsString().equals("close") ){
            this.dataExtractorThread.interrupt();
            this.dataExtractorThread = null;
            this.session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "User closed connection."));
            this.session = null;
        }

        if( json.get("action").getAsString().equals("stop") ){
            this.dataExtractorThread.interrupt();
            this.dataExtractorThread = null;
        }

        if( json.get("action").getAsString().equals("start") ){
            JsonArray fields = json.get("fields").getAsJsonArray();
            DataExtractorBuilder deb = new DataExtractorBuilder();

            for(int i = 0; i < fields.size(); i++){
                String field = fields.get(i).getAsString();
                JsonArray fieldInfo = json.get(field).getAsJsonArray();
                deb.addFieldName(field);

                for (int j = 0; j < fieldInfo.size(); j++) {
                    deb.addParameterData(field, fieldInfo.get(j).getAsString());
                }
            }

            dataExtractorThread = new Thread(new DataExtractor(deb.getFieldNames(), deb.getDataParams(), this.session));
            dataExtractorThread.start();
        }

        if( json.get("action").getAsString().equals("stayalive") ){
            session.getBasicRemote().sendText("{action:'stayalive'}");
        }
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason)
    {
        System.out.println("Socket Closed: " + reason);
        this.session = null;
        this.dataExtractorThread.interrupt();
        this.dataExtractorThread = null;
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        try {
            this.session.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "Error handling message. Closing socket."));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.session = null;
        this.dataExtractorThread.interrupt();
        this.dataExtractorThread = null;
        cause.printStackTrace(System.err);
    }
}
