package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.lang.reflect.Field;

public class Conditions {

    public Pair<ConditionsTypes, Integer> age;
    public Pair<ConditionsTypes, String> name;
    public Pair<ConditionsTypes, Integer> cash;
    public Pair<ConditionsTypes, Integer> credit;
    public Pair<ConditionsTypes, Integer> moneyFlow;
    public Pair<ConditionsTypes, Integer> mapPosition;
    public Pair<ConditionsTypes, Integer> currentTime;
    public Pair<ConditionsTypes, SeasonType> seasonType;
    public Single<Boolean> check(User user, SeasonType st) throws IllegalAccessException {
        return Single.create(singleEmitter -> {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field f : fields) {
                String name = f.getName();
                Object obj = f.get(this);
                if (obj == null) continue;
                switch (name) {
                    case ("name"):
                        if (((Pair<ConditionsTypes, String>) obj).getValue() != user.getName()) singleEmitter.onSuccess(false);
                        break;
                    case ("seasonType"):
                        if (((Pair<ConditionsTypes, SeasonType>) obj).getValue() != st) singleEmitter.onSuccess(false);
                        break;
                    default:
                        switch (((Pair<ConditionsTypes, Integer>) obj).getKey()) {
                            case EQUALS:
                                if (((Pair<ConditionsTypes, Integer>) obj).getValue() != user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                break;
                            case LESS:
                                if (((Pair<ConditionsTypes, Integer>) obj).getValue() <= (Integer) user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                break;
                            case MORE:
                                if (((Pair<ConditionsTypes, Integer>) obj).getValue() >= (Integer) user.getRequiredParameter(name))
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
