package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import com.google.gson.*;


import javax.annotation.Nullable;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EventReader {

    private static EventsRepository eventsRepository;

    @Nullable
    public EventsRepository getEventsRepository(DialogFactory dialogFactory){
        if(eventsRepository == null){
            JsonParser parser = new JsonParser();
            try(FileReader reader = new FileReader("src/main/resources/data/testJSON.json")){

                eventsRepository = new EventsRepository();
                JsonObject jsonObj = (JsonObject) parser.parse(reader);

                List<GoodEvent> goodTarget = new ArrayList<>();
                List<BadEvent> badTarget = new ArrayList<>();

                JsonArray good_arr = (JsonArray) jsonObj.get("goodEventList");
                for (int i = 0; i < good_arr.size(); ++i) {
                    goodTarget.add(new GoodEvent(dialogFactory));
                }

                JsonArray bad_arr = (JsonArray) jsonObj.get("badEventList");
                for (int i = 0; i < bad_arr.size(); ++i) {
                    badTarget.add(new BadEvent(dialogFactory));
                }

                parseListJson(good_arr, GoodEvent.class, goodTarget);
                parseListJson(bad_arr, BadEvent.class, badTarget);

                eventsRepository.setGoodEventList(goodTarget);
                eventsRepository.setBadEventList(badTarget);

                return eventsRepository;
            } catch (Exception e){
                System.out.println("Parsing Error " + e);
            }
            return null;
        } else {
            return eventsRepository;
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
