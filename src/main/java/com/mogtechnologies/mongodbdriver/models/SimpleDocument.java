package com.mogtechnologies.mongodbdriver.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.mogtechnologies.mongodbdriver.DatabaseController;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;


@Entity("simpledocuments")
@Indexes(
        @Index(value = "objectId", fields = @Field("objectId"))
)
public class SimpleDocument {

    @Id
    private ObjectId objectId;

    @Indexed(value= IndexDirection.DESC, name="id", unique=true, dropDups=true)
    private Integer id;
    private String title;
    private String text;

    public SimpleDocument(JsonNode jsonObject) {

        this.objectId = ObjectId.get();

        // Get last id
        Document sort = new Document();
        sort.put("id", -1);

        FindIterable<Document> cursor = DatabaseController
                                            .getInstance()
                                                .getCollection("simpledocuments")
                                                    .find()
                                                    .sort(sort)
                                                    .limit(1);

        if( cursor.first() == null )
            this.id = 0;
        else
            this.id = cursor.first().getInteger("id")+1;

        this.title = jsonObject.get("title").asText();
        this.text = jsonObject.get("text").asText();

        // TODO: There should be a better way...
        if( id == null || title == null || title.equals("") || text == null || text.equals("")) {
            this.id = null;
            this.title = null;
            this.text = null;
        }
    }

    @JsonProperty("objectId")
    public ObjectId getObjectId() { return objectId; }
    public void setObjectId(ObjectId objectId) { this.objectId = objectId; }

    @JsonProperty("id")
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    @JsonProperty("title")
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    @JsonProperty("text")
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
