package com.tweetapp.api.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Miguel Bronzovic.
 */
public class DateTimeTypeConverter implements JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        // improve this accordingly...
        Date d = new Date(json.getAsString());

        DateTime dt = new DateTime(d);

        return dt;
    }
}