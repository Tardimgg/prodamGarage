package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import com.google.gson.*;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;


import javax.annotation.Nullable;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventReader {

    //    private static EventsRepository eventsRepository;
    private static String defaultPath = "src/main/resources/data/testJSON.json";
    private static HashMap<String, EventsRepository> data = new HashMap<>();

    public static Single<EventsRepository> getEventsRepository(DialogFactory dialogFactory){
        return getEventsRepository(dialogFactory, defaultPath);
    }

    public static Single<EventsRepository> getEventsRepository(DialogFactory dialogFactory, String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (FileReader reader = new FileReader(path)) {

                    EventsRepository eventsRepository = new EventsRepository();
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

                    data.put(path, eventsRepository);

                    singleSubscriber.onSuccess(eventsRepository);
//                    return eventsRepository;
                } catch (Exception e) {
                    singleSubscriber.onError(new Throwable("parsing error" + e));
                }
//                return null;
            } else {
                singleSubscriber.onSuccess(data.get(path));
//                return data.get(path);
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
