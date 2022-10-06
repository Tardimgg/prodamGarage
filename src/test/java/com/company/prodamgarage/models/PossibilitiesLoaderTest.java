package com.company.prodamgarage.models;

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

import static org.junit.jupiter.api.Assertions.*;

class PossibilitiesLoaderTest {

    @Test
    void getPossibilitiesRepositoryTest() {
        PossibilitiesRepository rep = PossibilitiesLoader.getPossibilitiesRepository( "src/test/resources/possibilities_json.json").blockingGet();

        assertNotNull(rep);
        List<Possibility> listOfPossibilities = rep.getPossibilities();
        assertNotNull(listOfPossibilities);

        assertEquals(listOfPossibilities.size(), 2);

        assertEquals(((Possibility) listOfPossibilities.get(0)).name, "Buy Garage");
        assertEquals(((Possibility) listOfPossibilities.get(0)).description, "Покупка гаража -- отличный способ войтив бизнес самогоноваренья!");
        assertEquals(((Possibility) listOfPossibilities.get(0)).userChanges.deltaAge, 1);
        assertEquals(((Possibility) listOfPossibilities.get(0)).userChanges.deltaMoneyFlow, 5);

        assertEquals(((Possibility) listOfPossibilities.get(1)).name, "Buy pen");
        assertEquals(((Possibility) listOfPossibilities.get(1)).description, "Продай ручку. Часто ли вам приходилось слышать эту фразу? Мы будем уникальны и попросим вас купить ручку. buy pen pls");
        assertEquals(((Possibility) listOfPossibilities.get(1)).userChanges.deltaAge, 1);
        assertEquals(((Possibility) listOfPossibilities.get(1)).userChanges.deltaMoneyFlow, 5);
        assertEquals(((Possibility) listOfPossibilities.get(1)).userChanges.deltaCredit, -21);
    }
}