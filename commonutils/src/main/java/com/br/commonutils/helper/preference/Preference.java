package com.br.commonutils.helper.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.br.commonutils.validator.Validator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class Preference {

    private static Preference preference;
    private static Context context;
    private static String name;
    private Gson gson;

    private Preference(Context context, String name) {
        this.context = context;
        this.name = name;

        this.gson = new Gson();
    }

    public static void init(@NonNull Context context, @NonNull String preferenceName) {
        preference = new Preference(context, preferenceName);
    }

    public static Preference getInstance() throws IllegalAccessException {
        if (!Validator.isValid(preference))
            throw new IllegalAccessException("Call init()");

        return preference;
    }

    private static SharedPreferences sharedPreferences() {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor editor() {
        return sharedPreferences().edit();
    }

    public void remove(@NonNull String... key) {
        SharedPreferences.Editor editor = editor();
        for (int i = 0; i < key.length; i++) {
            editor.remove(key[i]);
            editor.commit();
        }
    }

    public void setString(@NonNull String key, String data) {
        SharedPreferences.Editor editor = editor();
        editor.putString(key, data);
        editor.commit();
    }

    public String getString(@NonNull String key) {
        return sharedPreferences().getString(key, null);
    }

    public void setBoolean(@NonNull String key, boolean data) {
        SharedPreferences.Editor editor = editor();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sharedPreferences().getBoolean(key, defaultValue);
    }

    public void setCollection(@NonNull String key, Collection data) {
        SharedPreferences.Editor editor = editor();
        editor.putString(key, gson.toJson(data));
        editor.commit();
    }

    public Collection getCollection(@NonNull String key, @NonNull Type type) {
//        Type type = new TypeToken<List<String>>() { }.getType();
        return gson.fromJson(sharedPreferences().getString(key, ""), type);
    }

    public Collection getCollection(@NonNull String key, @NonNull TypeToken typeToken) {
        return getCollection(key, typeToken.getType());
    }

    public void setObject(@NonNull String key, Object object) {
        SharedPreferences.Editor editor = editor();
        editor.putString(key, gson.toJson(object));
        editor.commit();
    }

    public <T> T getObject(@NonNull String key, @NonNull Class<T> aClass) {
        return gson.fromJson(sharedPreferences().getString(key, ""), aClass);
    }
}
