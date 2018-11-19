package com.br.commonutils.helper.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Preference {

    private Context context;
    private String name;
    private Gson gson;

    private Preference(Context context, String name) {
        this.context = context;
        this.name = name;

        this.gson = new Gson();
    }

    public static Preference make(@NonNull Context context, @NonNull String name) {
        return new Preference(context, name);
    }

    public void save(@NonNull String key, Object object) {
        SharedPreferences.Editor editor = editor();
        editor.putString(key, gson.toJson(object));
        editor.commit();
    }

    public String retrieve(@NonNull String key) {
        return sharedPreferences().getString(key, null);
    }

    public <T> T retrieve(@NonNull String key, @NonNull Class<T> aClass) {
        return gson.fromJson(sharedPreferences().getString(key, ""), aClass);
    }

    public <T> T retrieve(@NonNull String key, @NonNull Type type) {
//        Type type = new TypeToken<List<String>>() { }.getType();
        return gson.fromJson(sharedPreferences().getString(key, ""), type);
    }

    public <T> List<T> retrieveAsList(@NonNull String key, @NonNull Class<T> aClass) {
        return gson.fromJson(sharedPreferences().getString(key, ""), TypeToken.getParameterized(List.class, aClass).getType());
    }

    public <K, V> Map<K, V> retrieveAsMap(@NonNull String key, @NonNull Class<K> keyType, @NonNull Class<V> valueType) {
        return gson.fromJson(sharedPreferences().getString(key, ""), TypeToken.getParameterized(Map.class, keyType, valueType).getType());
    }

    public void remove(@NonNull String... key) {
        SharedPreferences.Editor editor = editor();

        for (int i = 0; i < key.length; i++) {
            editor.remove(key[i]);
            editor.commit();
        }
    }

    private SharedPreferences sharedPreferences() {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor editor() {
        return sharedPreferences().edit();
    }
}
