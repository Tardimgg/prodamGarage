package com.company.prodamgarage.models;

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
        List<Possibility> listOfPossibilities = rep.getApartmentPossibilities();
        List<Possibility> listOfEdPossibilities = rep.getEducationPossibilities();
        assertNotNull(listOfPossibilities);
        assertNotNull(listOfEdPossibilities);

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


        assertEquals(((Possibility) listOfEdPossibilities.get(0)).userChanges.deltaMoneyFlow, 3);
        assertEquals(((Possibility) listOfEdPossibilities.get(1)).userChanges.deltaMoneyFlow, 33);
        assertEquals(((Possibility) listOfEdPossibilities.get(2)).userChanges.deltaMoneyFlow, 333);

    }
}