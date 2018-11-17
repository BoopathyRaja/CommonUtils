package com.br.commonutils.helper.rest;

import android.support.annotation.NonNull;

import com.br.commonutils.provider.Task;
import com.br.commonutils.validator.Validator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestHelper {

    private final String baseUri;
    private final BodyType bodyType;
    private Map<Type, Object> typeAdapters;

    public RestHelper(@NonNull String baseUri) {
        this(baseUri, BodyType.JSON);
    }

    public RestHelper(@NonNull String baseUri, @NonNull BodyType bodyType) {
        if (!Validator.isValid(baseUri))
            throw new IllegalArgumentException("Invalid baseUri");

        this.baseUri = baseUri;
        this.bodyType = bodyType;
        this.typeAdapters = new HashMap<>();
    }

    private static void registerTypeAdapter(GsonBuilder gsonBuilder, Class<?> clazz) {
        gsonBuilder.registerTypeAdapter(clazz, new SimpleDeserializer());

        Type type = ParameterizedTypeImpl.make(List.class, new Type[]{clazz}, null);
        gsonBuilder.registerTypeAdapter(type, new ListDeserializer());
    }

    public void addTypeAdapters(Type... types) {
        if (Validator.isValid(types) && types.length > 0) {
            for (Type type : types) {
                this.typeAdapters.put(type, new BaseTypeAdapter());
            }
        }
    }

    public void addTypeAdapters(Type type, Object typeAdapter) {
        this.typeAdapters.put(type, typeAdapter);
    }

    public void sendPost(String uri, Object payload, @NonNull Type type, @NonNull Task task) {
        sendPost(uri, payload, type, new ArrayList<>(), task);
    }

    public void sendPost(String uri, Object payload, @NonNull Type type, List<? extends Param> params, @NonNull Task task) {
        RestExecutor<Object> restExecutor = new RestExecutor<>(baseUri, task);
        restExecutor.setUri(uri);
        restExecutor.setPayload(payload);
        restExecutor.setResponseType(type);
        restExecutor.setMethodType(MethodType.POST);
        restExecutor.setBodyType(bodyType);
        restExecutor.setGson(gson());
        restExecutor.execute(params.toArray(new Param[0]));
    }

    public void sendGet(String uri, @NonNull Type type, @NonNull Task task) {
        sendGet(uri, type, new ArrayList<>(), task);
    }

    public void sendGet(String uri, @NonNull Type type, List<? extends Param> params, @NonNull Task task) {
        RestExecutor<Object> restExecutor = new RestExecutor<>(baseUri, task);
        restExecutor.setUri(uri);
        restExecutor.setPayload(null);
        restExecutor.setResponseType(type);
        restExecutor.setMethodType(MethodType.GET);
        restExecutor.setBodyType(bodyType);
        restExecutor.setGson(gson());
        restExecutor.execute(params.toArray(new Param[0]));
    }

    public void sendPut(String uri, Object payload, @NonNull Type type, @NonNull Task task) {
        sendPut(uri, payload, type, new ArrayList<>(), task);
    }

    public void sendPut(String uri, Object payload, @NonNull Type type, List<? extends Param> params, @NonNull Task task) {
        RestExecutor<Object> restExecutor = new RestExecutor<>(baseUri, task);
        restExecutor.setUri(uri);
        restExecutor.setPayload(payload);
        restExecutor.setResponseType(type);
        restExecutor.setMethodType(MethodType.PUT);
        restExecutor.setBodyType(bodyType);
        restExecutor.setGson(gson());
        restExecutor.execute(params.toArray(new Param[0]));
    }

    public void sendDelete(String uri, @NonNull Type type, @NonNull Task task) {
        sendDelete(uri, type, new ArrayList<>(), task);
    }

    public void sendDelete(String uri, @NonNull Type type, List<? extends Param> params, @NonNull Task task) {
        RestExecutor<Object> restExecutor = new RestExecutor<>(baseUri, task);
        restExecutor.setUri(uri);
        restExecutor.setPayload(null);
        restExecutor.setResponseType(type);
        restExecutor.setMethodType(MethodType.DELETE);
        restExecutor.setBodyType(bodyType);
        restExecutor.setGson(gson());
        restExecutor.execute(params.toArray(new Param[0]));
    }

    private Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();

//        Type type = new TypeToken<List<String>>() { }.getType();
//        registerTypeAdapter(gsonBuilder, String.class);

        for (Type type : typeAdapters.keySet()) {
            gsonBuilder.registerTypeAdapter(type, typeAdapters.get(type));
        }

        return gsonBuilder.create();
    }

    private static class SimpleDeserializer implements JsonDeserializer {

        private static Gson gson = new Gson();

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return gson.fromJson(json, typeOfT);
        }
    }

    private static class ListDeserializer implements JsonDeserializer {

        private static Gson gson = new Gson();

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Type tType = (typeOfT instanceof ParameterizedType) ? ((ParameterizedType) typeOfT).getActualTypeArguments()[0] : Object.class;
            Type type = ParameterizedTypeImpl.make(List.class, new Type[]{tType}, null);

            return gson.fromJson(json, type);
        }
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {

        private Type[] actualTypeArguments;
        private Class<?> rawType;
        private Type ownerType;

        private ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            this.actualTypeArguments = actualTypeArguments;
            this.rawType = rawType;

            if (ownerType != null)
                this.ownerType = ownerType;
            else
                this.ownerType = rawType.getDeclaringClass();

            validateConstructorArguments();
        }

        public static ParameterizedTypeImpl make(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            return new ParameterizedTypeImpl(rawType, actualTypeArguments, ownerType);
        }

        private void validateConstructorArguments() {
            TypeVariable[] formals = rawType.getTypeParameters();

            if (formals.length != actualTypeArguments.length)
                throw new MalformedParameterizedTypeException();
        }

        public Type[] getActualTypeArguments() {
            return actualTypeArguments.clone();
        }

        public Class<?> getRawType() {
            return rawType;
        }

        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) obj;

                if (this == type)
                    return true;

                Type thatOwner = type.getOwnerType();
                Type thatRawType = type.getRawType();

                return (ownerType == null ? thatOwner == null : ownerType.equals(thatOwner)) && (rawType == null ? thatRawType == null : rawType.equals(thatRawType)) && Arrays.equals(actualTypeArguments, type.getActualTypeArguments());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(actualTypeArguments) ^ (ownerType == null ? 0 : ownerType.hashCode()) ^ (rawType == null ? 0 : rawType.hashCode());
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (ownerType != null) {
                if (ownerType instanceof Class)
                    sb.append(((Class) ownerType).getName());
                else
                    sb.append(ownerType.toString());

                sb.append(".");

                if (ownerType instanceof ParameterizedTypeImpl) {
                    sb.append(rawType.getName().replace(((ParameterizedTypeImpl) ownerType).rawType.getName() + "$", ""));
                } else
                    sb.append(rawType.getName());
            } else {
                sb.append(rawType.getName());
            }

            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                sb.append("<");

                boolean first = true;

                for (Type t : actualTypeArguments) {
                    if (!first)
                        sb.append(", ");

                    if (t instanceof Class)
                        sb.append(((Class) t).getName());
                    else
                        sb.append(t.toString());

                    first = false;
                }

                sb.append(">");
            }

            return sb.toString();
        }
    }
}
