package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.StdJsonParser;
import com.company.prodamgarage.models.conditions.Conditions;
import com.company.prodamgarage.models.conditions.ConditionsTypes;
import com.company.prodamgarage.models.eventModels.*;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.google.gson.*;
import io.reactivex.Single;


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
                            Pair.create(NotificationEvent.class, Optional.of(List.of(Pair.create(DialogFactory.class, dialogFactory)))),
                            Pair.create(SelectionEvent.class, Optional.of(List.of(Pair.create(DialogFactory.class, dialogFactory)))),
                            Pair.create(UserChanges.class, Optional.empty()),
                            Pair.create(Conditions.class, Optional.empty()),
                            Pair.create(ConditionsTypes.class, Optional.empty()),
//                            Pair.create(Pair.class, Optional.of(List.of(
//                                    Pair.create(String.class.getClass(), String.class),
//                                    Pair.create(UserChanges.class.getClass(), UserChanges.class)
//                            )))
                            Pair.create(Pair.class, Optional.empty())
                    );

                    List<Event> goodEvents = StdJsonParser.parseListJson(good_arr, allTypesObj, Event.class);
                    List<Event> badEvents = StdJsonParser.parseListJson(bad_arr, allTypesObj, Event.class);

                    if (!checkingCorrectnessListEvents(goodEvents) || !checkingCorrectnessListEvents(badEvents)) {
                        singleSubscriber.onError(new JsonParseException("parsing error, json is not complete"));
                        return;
                    }

                    eventsRepository.setGoodEventList(goodEvents);
                    eventsRepository.setBadEventList(badEvents);

                    data.put(path, eventsRepository);

                    singleSubscriber.onSuccess(eventsRepository);
                } catch (Exception e) {
                    singleSubscriber.onError(new RuntimeException("parsing error " + e));
                }
            } else {
                singleSubscriber.onSuccess(data.get(path));
            }
        });
    }

    private static boolean checkingCorrectnessListEvents(List<Event> events) {
        for (Event event: events) {
            if (!checkingCorrectnessEvents(event)) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkingCorrectnessEvents(Event event) {
        if (event instanceof NotificationEvent locEvent) {
            return locEvent.mainText != null && locEvent.title != null;

        } else if (event instanceof SelectionEvent locEvent) {
            return locEvent.title != null && locEvent.mainText != null && locEvent.userChanges != null; // temp code


        } else if (event instanceof PossibilitiesEvent) {

        }
        return true;
    }
}
