package com.company.prodamgarage;

import com.company.prodamgarage.models.conditions.Conditions;
import com.company.prodamgarage.models.conditions.ConditionsTypes;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.User;

public class test {
    public static void main(String[] args) throws IllegalAccessException {
        Conditions c = new Conditions();
        c.age = Pair.create(ConditionsTypes.EQUALS, 123);
        c.name = Pair.create(ConditionsTypes.LESS, "papa");
        c.seasonType = Pair.create(ConditionsTypes.EQUALS, SeasonType.AUTUMN);
        User user=new User();
        user.setMoneyFlow(4);
        user.setName("papa");
        user.setAge(123);
        SeasonType st = SeasonType.AUTUMN;
        System.out.println(c.check(user, st).blockingGet());
    }
}
