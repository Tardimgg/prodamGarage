package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.Int;
import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.lang.reflect.Field;

public class Conditions {

    public Pair<ConditionsTypesWrapper, Int> age;
    public Pair<ConditionsTypesWrapper, String> name;
    public Pair<ConditionsTypesWrapper, Int> cash;
    public Pair<ConditionsTypesWrapper, Int> credit;
    public Pair<ConditionsTypesWrapper, Int> moneyFlow;
    public Pair<ConditionsTypesWrapper, Int> mapPosition;
    public Pair<ConditionsTypesWrapper, Int> currentTime;
    public Pair<ConditionsTypesWrapper, SeasonType> seasonType;
    public Single<Boolean> check(User user, SeasonType st) throws IllegalAccessException {
        return Single.create(singleEmitter -> {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field f : fields) {
                String name = f.getName();
                Object obj = f.get(this);
                if (obj == null) continue;
                switch (name) {
                    case ("name"):
                        if (((Pair<ConditionsTypesWrapper, String>) obj).getValue() != user.getName()) singleEmitter.onSuccess(false);
                        break;
                    case ("seasonType"):
                        if (((Pair<ConditionsTypesWrapper, SeasonType>) obj).getValue() != st) singleEmitter.onSuccess(false);
                        break;
                    default:
                        switch (((Pair<ConditionsTypesWrapper, Int>) obj).getKey().value) {
                            case EQUALS:
                                if (((Pair<ConditionsTypesWrapper, Int>) obj).getValue() != user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                break;
                            case LESS:
                                if (((Pair<ConditionsTypesWrapper, Int>) obj).getValue().value <= (Integer) user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                break;
                            case MORE:
                                if (((Pair<ConditionsTypesWrapper, Int>) obj).getValue().value >= (Integer) user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                break;
                        }
                        break;
                }
            }
            singleEmitter.onSuccess(true);
        });
    };

}
