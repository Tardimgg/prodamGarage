package com.company.prodamgarage.models.conditions;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.SeasonType;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class Conditions {

    public Pair<ConditionsTypes, Integer> age;
    public Pair<ConditionsTypes, String> name;
    public Pair<ConditionsTypes, Integer> cash;
    public Pair<ConditionsTypes, Integer> credit;
    public Pair<ConditionsTypes, Integer> moneyFlow;
    public Pair<ConditionsTypes, Integer> mapPosition;
    public Pair<ConditionsTypes, Integer> currentTime;
    public Pair<ConditionsTypes, SeasonType> seasonType;

    public Single<Boolean> check(User user, SeasonType seasonType) {
        return Single.create(singleEmitter -> {

            singleEmitter.onSuccess(true);
        });
    }

}
