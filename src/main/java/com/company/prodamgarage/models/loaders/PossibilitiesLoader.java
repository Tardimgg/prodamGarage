package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.models.eventModels.EventType;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.MapRepository;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Single;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PossibilitiesLoader {
    private static String defaultPath = "src/main/resources/data/possibilitiesJSON.json";
    private static HashMap<String, PossibilitiesRepository> data = new HashMap<>();

    public static Single<PossibilitiesRepository> getPossibilitiesRepository() {
        return getPossibilitiesRepository(defaultPath);
    }

    public static Single<PossibilitiesRepository> getPossibilitiesRepository(String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (FileReader reader = new FileReader(path)) {
                    PossibilitiesRepository possibilitiesRepository = new PossibilitiesRepository();
                    JsonObject jsonObj = (JsonObject) parser.parse(reader);

                    List<BusinessPossibility> targetBisRepository = new ArrayList<>();
                    List<EducationPossibility> targetEdRepository = new ArrayList<>();

                    JsonArray BusinessPossibility_arr = (JsonArray) jsonObj.get("businessPossibilitiesList");
                    JsonArray EducationPossibility_arr = (JsonArray) jsonObj.get("educationPossibilitiesList");

                    for (int i = 0; i < BusinessPossibility_arr.size(); ++i) {
                        targetBisRepository.add(new BusinessPossibility());
                    }
                    for (int i = 0; i < EducationPossibility_arr.size(); ++i) {
                        targetEdRepository.add(new EducationPossibility());
                    }

                    parseListJson(BusinessPossibility_arr, BusinessPossibility.class, targetBisRepository);
                    parseListJson(EducationPossibility_arr, EducationPossibility.class, targetEdRepository);

                    possibilitiesRepository.setBusinessPossibilities(targetBisRepository);
                    possibilitiesRepository.setEducationPossibilities(targetEdRepository);

                    data.put(path, possibilitiesRepository);
                    singleSubscriber.onSuccess(possibilitiesRepository);
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
                if (field.getType().equals(String.class)) {
                    field.set(target, elem.getAsString());

                } else if (field.getType().equals(int.class)) {
                    field.set(target, elem.getAsInt());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}

