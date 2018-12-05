package com.br.commonutils.helper.jsonizer;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;

public class JacksonJsonizer {

    private static JacksonJsonizer jacksonJsonizer = new JacksonJsonizer();
    private ObjectMapper objectMapper;

    private JacksonJsonizer() {
        objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JacksonJsonizer getInstance() {
        return jacksonJsonizer;
    }

    public String to(@NonNull Object data) {
        String retVal = null;

        try {
            retVal = objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public Map toMap(@NonNull Object data) {
        return objectMapper.convertValue(data, Map.class);
    }

    public <T> T from(@NonNull String data, @NonNull Class<T> clazz) {
        T retVal = null;

        try {
            if (clazz.equals(String.class))
                retVal = (T) data;
            else
                retVal = objectMapper.readValue(data, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public <T> T from(@NonNull String data, @NonNull Type type) {
        T retVal = null;

        try {
            retVal = objectMapper.readValue(data, makeType(type));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public <T> T from(@NonNull Map data, @NonNull Class<T> clazz) {
        return from(to(data), clazz);
    }

    public <T> T from(@NonNull Map data, @NonNull Type type) {
        return from(to(data), type);
    }

    private JavaType makeType(@NonNull Type type) {
        return objectMapper.getTypeFactory().constructType(type);
    }
}
