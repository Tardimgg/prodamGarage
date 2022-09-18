package com.company.prodamgarage.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nullable;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapReader {
    private static String defaultPath = "src/main/resources/data/mapJSON.json";
    private static HashMap<String, MapRepository> data = new HashMap<>();

    @Nullable
    public MapRepository getMapRepository(){
        return getMapRepository(defaultPath);
    }

    @Nullable
    public MapRepository getMapRepository(String path) {
        if(!data.containsKey(path)){
            JsonParser parser = new JsonParser();
            try(FileReader reader = new FileReader(path)){

                MapRepository mapRepository = new MapRepository();
                JsonObject jsonObj = (JsonObject) parser.parse(reader);

                List<Map> mapTarget = new ArrayList<>();

                JsonArray map_arr = (JsonArray) jsonObj.get("mapCellList");
                for (int i = 0; i < map_arr.size(); ++i) {
                    mapTarget.add(new Map());
                }


                parseListJson(map_arr, Map.class, mapTarget);

                mapRepository.setMapList(mapTarget);

                data.put(path, mapRepository);

                return mapRepository;
            } catch (Exception e){
                System.out.println("Parsing Error " + e);
            }
            return null;
        } else {
            return data.get(path);
        }
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
        for (String key: source.keySet()) {
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
