package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.NotificationEvent;
import com.company.prodamgarage.models.eventModels.SelectionEvent;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventReaderTest {

    @Test
    void getEventsRepositoryDefaultTest() {
        EventsRepository rep = EventReader.getEventsRepository(null, "src/test/resources/event_json.json").blockingGet();

        assertNotNull(rep);
        List<Event> listOfGoodEvent = rep.getGoodEventList();
        assertNotNull(listOfGoodEvent);

        assertEquals(listOfGoodEvent.size(), 2);

        assertEquals(((NotificationEvent) listOfGoodEvent.get(0)).title, "goodName");
        assertEquals(((NotificationEvent) listOfGoodEvent.get(0)).text, "text");

        assertEquals(((SelectionEvent) listOfGoodEvent.get(1)).name, "goodName2");
        assertEquals(((SelectionEvent) listOfGoodEvent.get(1)).text, "");
        assertEquals(((SelectionEvent) listOfGoodEvent.get(1)).moneyBonus, 132);

        List<Event> listOfBadEvent = rep.getBadEventList();
        assertNotNull(listOfBadEvent);

        assertEquals(listOfBadEvent.size(), 2);

        assertEquals(((SelectionEvent) listOfBadEvent.get(0)).name, "badName1");
        assertEquals(((SelectionEvent) listOfBadEvent.get(0)).text, "12wef");
        assertEquals(((SelectionEvent) listOfBadEvent.get(0)).moneyBonus, 4234);

        assertEquals(((NotificationEvent) listOfBadEvent.get(1)).title, "badName2");
        assertEquals(((NotificationEvent) listOfBadEvent.get(1)).text, "12");
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
        List<Event> listOfGoodEvent = rep.getGoodEventList();

        assertNotNull(listOfGoodEvent);

        assertEquals(listOfGoodEvent.size(), 1);

        assertEquals(((SelectionEvent) listOfGoodEvent.get(0)).name, "\n");
        assertEquals(((SelectionEvent) listOfGoodEvent.get(0)).text, "\t");
        assertEquals(((SelectionEvent) listOfGoodEvent.get(0)).moneyBonus, -2);

        List<Event> listOfBadEvent = rep.getBadEventList();
        assertNotNull(listOfBadEvent);

        assertEquals(listOfBadEvent.size(), 0);
    }
}