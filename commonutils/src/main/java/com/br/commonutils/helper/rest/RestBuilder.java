package com.br.commonutils.helper.rest;

import android.support.annotation.NonNull;

import com.br.commonutils.provider.Task;
import com.br.commonutils.validator.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestBuilder {

    private final String baseUri;
    private String path;
    private List<Param> params;
    private MethodType methodType;
    private Object payload;
    private BodyType bodyType;
    private Type responseType;
    private Map<Type, Object> typeAdapters;

    private RestBuilder(String baseUri) {
        this.baseUri = baseUri;
        this.bodyType = BodyType.JSON;
        this.params = new ArrayList<>();
        this.typeAdapters = new HashMap<>();
    }

    public static RestBuilder make(@NonNull String baseUri) {
        return new RestBuilder(baseUri);
    }

    public RestBuilder path(@NonNull String path) {
        this.path = path;
        return this;
    }

    public RestBuilder queryParam(@NonNull QueryParam param) {
        this.params.add(param);
        return this;
    }

    public RestBuilder headerParam(@NonNull HeaderParam param) {
        this.params.add(param);
        return this;
    }

    public RestBuilder methodType(@NonNull MethodType methodType) {
        this.methodType = methodType;
        return this;
    }

    public RestBuilder payload(Object payload) {
        this.payload = payload;
        return this;
    }

    public RestBuilder bodyType(@NonNull BodyType bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public RestBuilder responseType(@NonNull Type responseType) {
        this.responseType = responseType;
        return this;
    }

    public RestBuilder typeAdapters(@NonNull Type... types) {
        if (Validator.isValid(types) && types.length > 0) {
            for (Type type : types) {
                this.typeAdapters.put(type, new BaseTypeAdapter());
            }
        }

        return this;
    }

    public RestBuilder typeAdapters(@NonNull Type type, @NonNull Class<? extends TypeAdapter> typeAdapter) {
        this.typeAdapters.put(type, typeAdapter);
        return this;
    }

    public void execute(@NonNull Task task) {
        switch (methodType) {
            case GET:
            case DELETE:
                payload = null;
                break;
        }

        RestExecutor restExecutor = new RestExecutor(baseUri, path, methodType, payload, bodyType, responseType, task);
        restExecutor.gson(gson());
        restExecutor.execute(params.toArray(new Param[0]));
    }

    private Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
//        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

//        Type type = new TypeToken<List<String>>() { }.getType();
//        registerTypeAdapter(gsonBuilder, String.class);

        for (Type type : typeAdapters.keySet()) {
            gsonBuilder.registerTypeAdapter(type, typeAdapters.get(type));
        }

        return gsonBuilder.create();
    }
}
