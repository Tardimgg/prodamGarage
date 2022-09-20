package com.company.prodamgarage.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Single;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MapReader {
    private static String defaultPath = "src/main/resources/data/mapJSON.json";
    private static HashMap<String, MapRepository> data = new HashMap<>();

    public static Single<MapRepository> getMapRepository() {
        return getMapRepository(defaultPath);
    }

    public static Single<MapRepository> getMapRepository(String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (FileReader reader = new FileReader(path)) {
                    MapRepository mapRepository = new MapRepository();
                    JsonObject jsonObj = (JsonObject) parser.parse(reader);
                    List<MapElement> mapElementTarget = new ArrayList<>();
                    JsonArray map_arr = (JsonArray) jsonObj.get("mapCellList");
                    for (int i = 0; i < map_arr.size(); ++i) {
                        mapElementTarget.add(new MapElement());
                    }
                    parseListJson(map_arr, MapElement.class, mapElementTarget);
                    mapRepository.setMapList(mapElementTarget);
                    data.put(path, mapRepository);
                    singleSubscriber.onSuccess(mapRepository);
                } catch (Exception e) {
                    singleSubscriber.onError(new Throwable("parsing error" + e));
                }
            } else {
                singleSubscriber.onSuccess(data.get(path));
            }
        });
    }

    private static <T> void parseListJson(JsonArray source, Class<T> tClass, List<T> target) throws NoSuchFieldException {
        if (target.size() < source.size()) {
            throw new IndexOutOfBoundsException("storage is too small");
        }
        for (int i = 0; i < source.size(); ++i) {
            JsonObject elemObj = (JsonObject) source.get(i);
            parseJson(elemObj, tClass, target.get(i));
        }
    }

    private static <T> void parseJson(JsonObject source, Class<T> tClass, T target) throws NoSuchFieldException {
        for (String key : source.keySet()) {
            Field field = tClass.getField(key);
            try {
                JsonElement elem = source.get(key);
                if (field.getType().equals(EventType.class)) {
                    field.set(target, EventType.valueOf(elem.getAsString()));

                } else if (field.getType().equals(SeasonType.class)) {
                    field.set(target, SeasonType.valueOf(elem.getAsString()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
