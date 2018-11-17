package com.br.commonutils.helper.jsonizer;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonJsonizer {

    private static GsonJsonizer gsonJsonizer = new GsonJsonizer();
    private Gson gson;

    private GsonJsonizer() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }


    public static GsonJsonizer getInstance() {
        return gsonJsonizer;
    }

    public String to(@NonNull Object data) {
        return gson.toJson(data);
    }

    public Map toMap(@NonNull Object data) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(gson.toJson(data), type);
    }

    public <T> T from(@NonNull String data, @NonNull Type type) {
        return gson.fromJson(data, type);
    }

    public <T> T from(@NonNull String data, @NonNull Class<T> clazz) {
        return gson.fromJson(data, clazz);
    }

    public <T> T from(@NonNull Map data, @NonNull Class<T> clazz) {
        String json = gson.toJson(data);
        return from(json, clazz);
    }
}
