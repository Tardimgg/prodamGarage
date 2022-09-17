package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventReaderTest {

    @Test
    void getEventsRepositoryDefaultTest() {
        EventsRepository rep = EventReader.getEventsRepository(null, "src/test/resources/event_json.json").blockingGet();

        assertNotNull(rep);
        List<GoodEvent> listOfGoodEvent = rep.getGoodEventList();
        assertNotNull(listOfGoodEvent);

        assertEquals(listOfGoodEvent.size(), 2);

        assertEquals(listOfGoodEvent.get(0).name, "goodName");
        assertEquals(listOfGoodEvent.get(0).text, "text");
        assertEquals(listOfGoodEvent.get(0).moneyBonus, 5);

        assertEquals(listOfGoodEvent.get(1).name, "goodName2");
        assertEquals(listOfGoodEvent.get(1).text, "");
        assertEquals(listOfGoodEvent.get(1).moneyBonus, 132);

        List<BadEvent> listOfBadEvent = rep.getBadEventList();
        assertNotNull(listOfBadEvent);

        assertEquals(listOfBadEvent.size(), 2);

        assertEquals(listOfBadEvent.get(0).name, "badName1");
        assertEquals(listOfBadEvent.get(0).text, "12wef");
        assertEquals(listOfBadEvent.get(0).moneyBonus, 4234);

        assertEquals(listOfBadEvent.get(1).name, "badName2");
        assertEquals(listOfBadEvent.get(1).text, "12");
        assertEquals(listOfBadEvent.get(1).moneyBonus, 234);
    }

    @Test
    void getEventsRepositoryFromNonExistentFile() {
        EventReader.getEventsRepository(null, "src/test/resources/lwwefobwef.json")
                .subscribe(new BiConsumerSingleObserver<>((eventsRepository, throwable) -> {
                    assertNull(eventsRepository);
                    assertNotNull(throwable);
                }));
    }

    @Test
    void getEventsRepository() {
        EventsRepository rep = EventReader.getEventsRepository(null, "src/test/resources/half_empty_event_json.json").blockingGet();

        assertNotNull(rep);
        List<GoodEvent> listOfGoodEvent = rep.getGoodEventList();
        assertNotNull(listOfGoodEvent);

        assertEquals(listOfGoodEvent.size(), 1);

        assertEquals(listOfGoodEvent.get(0).name, "\n");
        assertEquals(listOfGoodEvent.get(0).text, "\t");
        assertEquals(listOfGoodEvent.get(0).moneyBonus, -2);

        List<BadEvent> listOfBadEvent = rep.getBadEventList();
        assertNotNull(listOfBadEvent);

        assertEquals(listOfBadEvent.size(), 0);
    }
}