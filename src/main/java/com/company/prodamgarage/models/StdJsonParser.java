package com.company.prodamgarage.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.List;

public class StdJsonParser {

    public static <T> void parseListJson(JsonArray source, Class<T> tClass, List<T> target) throws NoSuchFieldException {
        if (target.size() < source.size()) {
            throw new IndexOutOfBoundsException("storage is too small");
        }
        for (int i = 0; i < source.size(); ++i) {
            JsonObject elemObj = (JsonObject) source.get(i);
            parseJson(elemObj, tClass, target.get(i));
        }
    }

    public static <T> void parseJson(JsonObject source, Class<T> tClass, T target) throws NoSuchFieldException {
        for (String key : source.keySet()) {
            Field field = tClass.getField(key);
            try {
                JsonElement elem = source.get(key);
                if (field.getType().equals(String.class)) {
                    field.set(target, elem.getAsString());

                } else if (field.getType().equals(Integer.TYPE)) {
                    field.set(target, elem.getAsInt());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
