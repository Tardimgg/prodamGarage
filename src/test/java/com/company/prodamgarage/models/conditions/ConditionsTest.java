package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.PropertyType;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.models.user.UserChanges;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionsTest {

    @Test
    void checkEqAge() throws IllegalAccessException {
        User user = User.getInstance().blockingGet();
        user.setAge(15);

        Conditions c = new Conditions();
        c.age = Pair.create(ConditionsTypes.EQUALS, 15);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        c.age.setValue(16);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

    }

    @Test
    void checkCmpAge() throws IllegalAccessException {
        User user = User.getInstance().blockingGet();
        user.setAge(15);

        Conditions c = new Conditions();
        c.age = Pair.create(ConditionsTypes.MORE, 10);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        c.age.setKey(ConditionsTypes.MORE);
        c.age.setValue(15);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);
    }

    @Test
    void checkProperty() throws IllegalAccessException {
        User user = User.getInstance().blockingGet();

        Conditions c = new Conditions();
        c.withoutProperty = Pair.create(PropertyType.HOUSE, "ДОМ");
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

        user.addProperties(PropertyType.HOUSE, "ДОМ", null);
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

    }

    @Test
    void checkProbability() throws IllegalAccessException {
        User user = User.getInstance().blockingGet();

        Conditions c = new Conditions();
        c.probability = 0;
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), false);

        c.probability = 1;
        assertEquals(c.check(user, SeasonType.AUTUMN).blockingGet(), true);

    }
}