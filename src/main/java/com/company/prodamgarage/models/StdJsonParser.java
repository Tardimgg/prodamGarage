package com.company.prodamgarage.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StdJsonParser {


    //  types: list of pair
    //      key: types of possible events
    //      value: list of pair
    //          key: type of the constructor parameter of the current event
    //          value: the value of this parameter of the current event
    public static <T> List<T> parseListJson(JsonArray source, List<Pair<Class<?>,
            Optional<List<Pair<Class<?>, ?>>>>> allTypes, Class<T> target) throws NoSuchFieldException, NoSuchMethodException {

        List<T> ans = new ArrayList<>();
        for (int i = 0; i < source.size(); ++i) {
            JsonObject elemObj = (JsonObject) source.get(i);
            ans.add(parseJson(elemObj, allTypes, target));
        }

        return ans;
    }


    public static <T> T parseJson(JsonObject source, List<Pair<Class<?>,
            Optional<List<Pair<Class<?>, ?>>>>> allTypes, Class<T> castTarget) throws NoSuchFieldException, NoSuchMethodException {

        if (!source.has("className")) {
            throw new NoSuchFieldException("className field is required");
        }

        String eventType = source.get("className").getAsString();
        var targetPair = allTypes.stream().filter((v) -> {
            int indexLastDot = v.getKey().getName().lastIndexOf('.');
            return v.getKey().getName().substring(indexLastDot + 1).equals(eventType);
        }).findFirst().orElseThrow(() -> new NoSuchFieldException("the required class was not found"));


        AtomicReference<T> ans = new AtomicReference<>();

        targetPair.getValue().ifPresentOrElse(pairs -> {
            List<Class<?>> constructParams = pairs.stream().map(Pair::getKey).collect(Collectors.toList());

            Constructor<?> ansConstructor;

            try {
                ansConstructor = targetPair.getKey().getConstructor(constructParams.toArray(Class<?>[]::new));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("the required class constructor was not found");
            }

            List<Object> constructParamsObj = pairs.stream().map(Pair::getValue).collect(Collectors.toList());

            try {
                ans.set((T) ansConstructor.newInstance(constructParamsObj.toArray(Object[]::new)));
            } catch (Exception e) {
                throw new RuntimeException("invalid data provided for constructor");
            }

        }, () -> {
            try {
                ans.set((T) targetPair.getKey().getConstructor().newInstance());
            } catch (Exception e) {
                throw new RuntimeException("the default constructor not found");
            }
        });

        for (String key: source.keySet()) {

            if (key.equals("className")) {
                continue;
            }

            if (Arrays.stream(targetPair.getKey().getFields()).filter((v) -> v.getName().equals(key)).findFirst().isEmpty()) {
                throw new NoSuchFieldException(targetPair.getKey().toString() + " does not contain the " + key +" field");
            }
            Field field = targetPair.getKey().getField(key);
            try {
                JsonElement elem = source.get(key);

                if (elem.isJsonArray()) {
                    field.set(ans.get(), parseListJson((JsonArray) elem, allTypes, field.getType()));

                } else if (elem.isJsonObject()) {
                    field.set(ans.get(), parseJson((JsonObject) elem, allTypes, field.getType()));

                } else {
                    if (field.getType().equals(String.class)) {
                        field.set(ans.get(), elem.getAsString());

                    } else if (field.getType().equals(Integer.TYPE)) {
                        field.set(ans.get(), elem.getAsInt());
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return ans.get();
    }
}
