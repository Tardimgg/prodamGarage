package com.company.prodamgarage.models;

import com.company.prodamgarage.models.dialog.factory.ConsoleDialogFactory;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.NotificationEvent;
import com.company.prodamgarage.models.eventModels.SelectionEvent;
import com.company.prodamgarage.models.loaders.EventReader;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.possibilityModels.PossibilitiesRepository;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EventLoaderTest {
    @Test
    void getPossibilitiesRepositoryTest() {
        EventsRepository rep = EventReader.getEventsRepository(new ConsoleDialogFactory(), "src/test/resources/event_json.json").blockingGet();

        assertNotNull(rep);
        List<Event> list1 = rep.getGoodEventList();
        List<Event> list2 = rep.getBadEventList();
        assertNotNull(list1);
        assertNotNull(list2);

        assertEquals(list1.size(), 2);

        assertEquals(((NotificationEvent) list1.get(0)).title, "testName0");
        assertEquals(((NotificationEvent) list1.get(0)).userChanges.deltaMoneyFlow, 5);
        assertEquals(((NotificationEvent) list1.get(0)).userChanges.deltaAge, 1);

        assertEquals(((SelectionEvent) list1.get(1)).title, "testName1");
        assertEquals(((SelectionEvent) list1.get(1)).userChanges.get(0).value.deltaMoneyFlow, 50);
        assertEquals(((SelectionEvent) list1.get(1)).userChanges.get(1).value.deltaMoneyFlow, 51);

        assertEquals(list2.size(), 2);

        assertEquals(((SelectionEvent) list2.get(0)).title, "testName2");
        assertEquals(((SelectionEvent) list2.get(0)).userChanges.get(0).value.deltaMoneyFlow, 55);
        assertEquals(((SelectionEvent) list2.get(0)).userChanges.get(1).value.deltaMoneyFlow, 54);

    }
}
