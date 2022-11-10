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

    public Pair<ConditionsTypes, Int> age;
    public Pair<ConditionsTypes, String> name;
    public Pair<ConditionsTypes, Int> cash;
    public Pair<ConditionsTypes, Int> credit;
    public Pair<ConditionsTypes, Int> moneyFlow;
    public Pair<ConditionsTypes, Int> mapPosition;
    public Pair<ConditionsTypes, Int> currentTime;
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
                        if (!((Pair<ConditionsTypes, String>) obj).getValue().equals(user.getName())) singleEmitter.onSuccess(false);
                        return;
                    case ("seasonType"):
                        if (((Pair<ConditionsTypes, SeasonType>) obj).getValue() != st) singleEmitter.onSuccess(false);
                        return;
                    default:
                        switch (((Pair<ConditionsTypes, Int>) obj).getKey()) {
                            case EQUALS:
                                if (((Pair<ConditionsTypes, Int>) obj).getValue() != user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);return;
                            case LESS:
                                if (((Pair<ConditionsTypes, Int>) obj).getValue().value <= (Integer) user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                return;
                            case MORE:
                                if (((Pair<ConditionsTypes, Int>) obj).getValue().value >= (Integer) user.getRequiredParameter(name))
                                    singleEmitter.onSuccess(false);
                                return;
                        }
                        break;
                }
            }
            singleEmitter.onSuccess(true);
        });
    };

}
