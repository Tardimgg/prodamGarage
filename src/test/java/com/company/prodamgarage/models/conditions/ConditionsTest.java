package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.PropertyType;
import com.company.prodamgarage.models.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConditionsTest {

    @Mock
    private User user;

    @Test
    void checkEqAge() throws IllegalAccessException {
        Mockito.when(user.getRequiredParameter("age")).thenReturn(15);

        Conditions c = new Conditions();
        c.age = Pair.create(ConditionsTypes.EQUALS, 15);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        c.age.setValue(16);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

    }

    @Test
    void checkCmpAge() throws IllegalAccessException {
        Mockito.when(user.getRequiredParameter("age")).thenReturn(15);

        Conditions c = new Conditions();
        c.age = Pair.create(ConditionsTypes.MORE, 10);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        c.age.setKey(ConditionsTypes.MORE);
        c.age.setValue(15);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);
    }

    @Test
    void checkProperty() throws IllegalAccessException {
        Mockito.when(user.checkProperties(PropertyType.HOUSE, "ДОМ")).thenReturn(false);

        Conditions c = new Conditions();
        c.withoutProperty = Pair.create(PropertyType.HOUSE, "ДОМ");
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        Mockito.when(user.checkProperties(PropertyType.HOUSE, "ДОМ")).thenReturn(true);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

    }

    @Test
    void checkProbability() throws IllegalAccessException {
        Conditions c = new Conditions();
        c.probability = 0;
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

        c.probability = 1;
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

    }
}