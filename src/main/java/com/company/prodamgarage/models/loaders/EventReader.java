package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.models.StdJsonParser;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.NotificationEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.SelectionEvent;
import com.company.prodamgarage.models.eventModels.Event;
import com.google.gson.*;
import io.reactivex.Single;
import javafx.util.Pair;


import java.io.FileReader;
import java.util.*;

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

                    List<Pair<Class<?>, Optional<List<Pair<Class<?>, ?>>>>> allTypesObj = Arrays.asList(
                            new Pair<>(NotificationEvent.class, Optional.of(List.of(new Pair<>(DialogFactory.class, dialogFactory)))),
                            new Pair<>(SelectionEvent.class, Optional.of(List.of(new Pair<>(DialogFactory.class, dialogFactory)))),
                            new Pair<>(UserChanges.class, Optional.empty())
                    );

                    List<Event> goodEvents = StdJsonParser.parseListJson(good_arr, allTypesObj, Event.class);
                    List<Event> badEvents = StdJsonParser.parseListJson(bad_arr, allTypesObj, Event.class);

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
}
