package com.br.commonutils.helper.rest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class BaseTypeAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    private String metaKey;

    public BaseTypeAdapter() {
        metaKey = "metaKey";
    }

    public BaseTypeAdapter(String metaKey) {
        this.metaKey = metaKey;
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            String packageName = ((Class) type).getPackage().getName();
            String className = jsonObject.get(metaKey).getAsString();

            Class<?> clazz = Class.forName(packageName + "." + className);
            return jsonDeserializationContext.deserialize(jsonElement, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonElement = jsonSerializationContext.serialize(object, object.getClass());

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.addProperty(metaKey, object.getClass().getSimpleName());

        return jsonElement;
    }
}
