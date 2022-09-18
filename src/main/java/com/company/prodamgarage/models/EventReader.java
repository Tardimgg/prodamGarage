package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.NotificationEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.SelectionEvent;
import com.google.gson.*;
import io.reactivex.Single;
import javafx.util.Pair;


import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

                    JsonArray good_arr = (JsonArray) jsonObj.get("goodEventList");
                    JsonArray bad_arr = (JsonArray) jsonObj.get("badEventList");

                    List<Pair<Class<? extends Event>, List<Pair<Class<?>, ?>>>> typesEvents = Arrays.asList(
                            new Pair<>(NotificationEvent.class, List.of(new Pair<>(DialogFactory.class, dialogFactory))),
                            new Pair<>(SelectionEvent.class, List.of(new Pair<>(DialogFactory.class, dialogFactory)))
                    );

//                    Pair<Class<? extends Event>, List<Pair<Class<?>, ?>>> notificationPair = new Pair<>(NotificationEvent.class, List.of(new Pair<>(DialogFactory.class, dialogFactory)));
//                    Pair<Class<? extends Event>, List<Pair<Class<?>, ?>>> selectionPair = new Pair<>(SelectionEvent.class, List.of(new Pair<>(DialogFactory.class, dialogFactory)));

//                    List<Pair<Class<? extends Event>, List<Pair<Class<?>, ?>>>> events = Arrays.asList(notificationPair, selectionPair);

                    List<Event> goodEvents = parseListJson(good_arr, typesEvents, Event.class);
                    List<Event> badEvents = parseListJson(bad_arr, typesEvents, Event.class);

                    eventsRepository.setGoodEventList(goodEvents);
                    eventsRepository.setBadEventList(badEvents);

                    data.put(path, eventsRepository);

                    singleSubscriber.onSuccess(eventsRepository);
                } catch (Exception e) {
                    singleSubscriber.onError(new Throwable("parsing error" + e));
                }
            } else {
                singleSubscriber.onSuccess(data.get(path));
            }
        });
    }

    //  types: list of pair
    //      key: types of possible events
    //      value: list of pair
    //          key: type of the constructor parameter of the current event
    //          value: the value of this parameter of the current event

    private static <T> List<T> parseListJson(JsonArray source, List<Pair<Class<? extends Event>, List<Pair<Class<?>, ?>>>> types,
                                             Class<T> target) throws NoSuchFieldException, NoSuchMethodException {

        List<T> ans = new ArrayList<>();
        for (int i = 0; i < source.size(); ++i) {
            JsonObject elemObj = (JsonObject) source.get(i);
            ans.add(parseJson(elemObj, types, target));
        }

        return ans;
    }


    private static <T> T parseJson(JsonObject source, List<Pair<Class<? extends Event>,
            List<Pair<Class<?>, ?>>>> types, Class<T> castTarget) throws NoSuchFieldException, NoSuchMethodException {

        if (!source.has("eventType")) {
            throw new NoSuchFieldException("eventType field is required");
        }

        String eventType = source.get("eventType").getAsString();
        var targetPair = types.stream().filter((v) -> {
            int indexLastDot = v.getKey().getName().lastIndexOf('.');
            return v.getKey().getName().substring(indexLastDot + 1).equals(eventType);
        }).findFirst().orElseThrow(() -> new NoSuchFieldException("the required class was not found"));

        List<Class<?>> constructParams = targetPair.getValue().stream().map(Pair::getKey).collect(Collectors.toList());

        Constructor<? extends Event> ansConstructor;

        try {
            ansConstructor = targetPair.getKey().getConstructor(constructParams.toArray(Class<?>[]::new));
        } catch (NoSuchMethodException e) {
            throw new NoSuchFieldException("the required class constructor was not found");
        }

        List<Object> constructParamsObj = targetPair.getValue().stream().map(Pair::getValue).collect(Collectors.toList());

        T ans;
        try {
            ans = (T) ansConstructor.newInstance(constructParamsObj.toArray(Object[]::new));
        } catch (Exception e) {
            throw new NoSuchMethodException("invalid data provided for constructor");
        }

        for (String key: source.keySet()) {

            if (key.equals("eventType")) {
                continue;
            }

            if (key.equals("userChanges")) {
                // add implementation userChanges parsing
                continue;
            }

            Field field = targetPair.getKey().getField(key);

            try {
                JsonElement elem = source.get(key);
                if (field.getType().equals(String.class)) {
                    field.set(ans, elem.getAsString());

                } else if (field.getType().equals(Integer.TYPE)) {
                    field.set(ans, elem.getAsInt());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return ans;
    }
}
