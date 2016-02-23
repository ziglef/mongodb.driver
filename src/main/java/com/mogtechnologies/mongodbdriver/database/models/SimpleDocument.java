package com.mogtechnologies.mongodbdriver.database.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 * Created by rgrandao on 23/02/2016.
 */
@Entity("simpledocuments")
@Indexes(
        @Index(value = "id", fields = @Field("id"))
)
public class SimpleDocument {
    @Id
    private ObjectId id;

    private String title;
    private String text;

}
