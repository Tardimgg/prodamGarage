package com.company.prodamgarage.models;

import com.company.prodamgarage.Pair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class StdJsonParser {


    //  types: list of pair
    //      key: types of possible events
    //      value: list of pair
    //          key: type of the constructor parameter of the current event
    //          value: the value of this parameter of the current event
    public static <T> List<T> parseListJson(JsonArray source, List<Pair<Class<?>,
            Optional<List<Pair<Class<?>, ?>>>>> allTypes, Class<T> target)
            throws NoSuchFieldException, NoSuchMethodException {

        List<T> ans = new ArrayList<>();
        for (int i = 0; i < source.size(); ++i) {
            JsonObject elemObj = (JsonObject) source.get(i);
            ans.add(parseJson(elemObj, allTypes, target));
        }

        return ans;
    }

    //  types: list of pair
    //      key: types of possible events
    //      value: list of pair
    //          key: type of the constructor parameter of the current event
    //          value: the value of this parameter of the current event
    public static <T> T parseJson(JsonObject source, List<Pair<Class<?>,
            Optional<List<Pair<Class<?>, ?>>>>> allTypes, Class<T> castTarget)
            throws NoSuchFieldException, NoSuchMethodException {

        if (!source.has("className")) {
            throw new NoSuchFieldException("className field is required");
        }

        String objName = source.get("className").getAsString();

        if (parsableClassesNames.contains(objName)) {
            return (T) parseEnumAndSome(objName, source);
        }

        var targetPair = allTypes.stream().filter((v) -> {
            return getClassName(v.getKey().getName()).equals(getClassName(objName));
        }).findFirst().orElseThrow(() -> new NoSuchFieldException("the required class (" + objName + ") was not found"));

        if (targetPair.getKey().isEnum() || parsableClasses.contains(targetPair.getKey())) {
            return (T) parseEnumAndSome(targetPair.getKey(), source);
        }

        AtomicReference<T> ans = new AtomicReference<>();
        targetPair.getValue().ifPresentOrElse(pairs -> {
            List<Class<?>> constructParams = pairs.stream().map(Pair::getKey).collect(Collectors.toList());

            Constructor<?> ansConstructor;
            try {
                ansConstructor = targetPair.getKey().getConstructor(constructParams.toArray(Class<?>[]::new));
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("the required class(" + targetPair.getKey() + ") constructor was not found");
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

            if (Arrays.stream(targetPair.getKey().getFields())
                    .filter((v) -> v.getName().equals(key))
                    .findFirst()
                    .isEmpty()) {
                throw new NoSuchFieldException(targetPair.getKey().toString() + " does not contain the " + key +" field");
            }

            Field field = targetPair.getKey().getField(key);
            try {
                JsonElement elem = source.get(key);

                if (elem.isJsonArray()) {
                    JsonArray elemArr = (JsonArray) elem;

                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    Type type = parameterizedType.getActualTypeArguments()[0];


                    for (JsonElement jsonElem: elemArr) {
                        if (jsonElem.isJsonObject()) {
                            JsonObject jsonObj = (JsonObject) jsonElem;
                            if (!jsonObj.has("className")) {
                                jsonObj.addProperty("className", getClassName(type.getTypeName()));
                            }
                        } else if (jsonElem.isJsonPrimitive()) {

                        }
                    } // problems with array of primitives !!

                    field.set(ans.get(), parseListJson(elemArr, allTypes, field.getType()));

                } else if (elem.isJsonObject()) {
                    if (!((JsonObject) elem).has("className")) {
                        ((JsonObject) elem).addProperty("className", getClassName(field.getType().toString()));
                    }
                    field.set(ans.get(), parseJson((JsonObject) elem, allTypes, field.getType()));

                } else {
                    Object value;
                    if (field.getType().equals(String.class)) {
                        value = elem.getAsString();

                    } else if (field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)) {
                        value = elem.getAsInt();

                    } else if (field.getType().equals(Float.TYPE) || field.getType().equals(Float.class)) {
                        value = elem.getAsFloat();

                    } else if (field.getType().isEnum()) {
                        // it is necessary to test // TESTED ^-^
                        value = Enum.valueOf((Class<Enum>) field.getType(), elem.getAsString());
                    } else {
                        value = field.getType().cast(elem.getAsString());
                    }

                    field.set(ans.get(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return ans.get();
    }


    private static final Set<Class> parsableClasses = Set.of(Integer.class, String.class, Enum.class);
    private static final Set<String> parsableClassesNames = Set.of("Integer", "String", "Enum");

    private static Object parseEnumAndSome(Class clazz, JsonObject jsonObject) {
        if (clazz.equals(String.class)) {
            return jsonObject.get("value").getAsString();

        } else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            return jsonObject.get("value").getAsInt();

        } else if (clazz.equals(Float.class) || clazz.equals(Float.TYPE)) {
            return jsonObject.get("value").getAsFloat();

        } else if (clazz.isEnum()) {
            return parseEnum(clazz, jsonObject);
        } else {
            return clazz.cast(jsonObject.get("value").getAsString());
        }
    }

    private static Object parseEnumAndSome(String clazz, JsonObject jsonObject) {
        return switch (clazz) {
            case "String" -> parseEnumAndSome(String.class, jsonObject);
            case "Integer" -> parseEnumAndSome(Integer.class, jsonObject);
            case "Enum" -> parseEnumAndSome(Enum.class, jsonObject);
            default -> null;
        };
    }

    private static Enum parseEnum(Class<Enum> enumClass, JsonObject jsonObject) {
        return Enum.valueOf(enumClass, jsonObject.get("value").getAsString());
    }

    private static String getClassName(String fullName) {
        int indexOfGenericParam = fullName.indexOf('<');

        int indexLastDot;
        if (indexOfGenericParam != -1) {
            fullName = fullName.substring(0, indexOfGenericParam);
        }

        indexLastDot = fullName.lastIndexOf('.');

        return fullName.substring(indexLastDot + 1);
    }
}
