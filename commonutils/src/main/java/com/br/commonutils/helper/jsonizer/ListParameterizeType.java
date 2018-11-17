package com.br.commonutils.helper.jsonizer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class ListParameterizeType<T> implements ParameterizedType {

    private Class<?> actualArguments;

    public ListParameterizeType(Class<T> actualArguments) {
        this.actualArguments = actualArguments;
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{actualArguments};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
