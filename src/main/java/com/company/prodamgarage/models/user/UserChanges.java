package com.company.prodamgarage.models.user;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.conditions.ConditionsTypes;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class UserChanges implements Serializable {

    public int deltaAge = 0;
    public int deltaCash = 0;
    public int deltaCredit = 0;
    public int deltaMoneyFlow = 0;
    public int deltaExpenses = 0;
    public int deltaAssets = 0;
    public int deltaPassive = 0;
    public int deltaFreeTime = 0;
    public List<Event> deferredEvents;
    public Pair<PropertyType, String> addedProperty;

    public Completable apply() {
        return Completable.create(completableEmitter -> {
            try {
                User user = User.getInstance().blockingGet();
                user.setAge(user.getAge() + deltaAge);
                user.setCash(user.getCash() + deltaCash);
                user.setCredit(user.getCredit() + deltaCredit);
                user.setMoneyFlow(user.getMoneyFlow() + deltaMoneyFlow);
                user.setExpenses(user.getExpenses() + deltaExpenses);
                user.setAssets(user.getAssets() + deltaAssets);
                user.setPassive(user.getPassive() + deltaPassive);
                user.setFreeTime(user.getFreeTime() + deltaFreeTime);

                if (deferredEvents != null) {
                    for (var val: deferredEvents) {
                        if (val.conditions.afterWhile != null) {
                            val.conditions.currentTime = Pair.create(
                                    ConditionsTypes.MORE,
                                    user.getCurrentTime() + val.conditions.afterWhile);
                        }
                    }
                    user.addDeferredEvents(deferredEvents);
                }
                if (addedProperty != null) {
                    user.addProperties(addedProperty.key, addedProperty.value, this);
                }

                completableEmitter.onComplete();

            } catch (RuntimeException e) {
                e.printStackTrace();
                completableEmitter.onError(e);
            }
        });
    }

    @Override
    public String toString() {
        return "UserChanges{" +
                "deltaAge=" + deltaAge +
                ", deltaCash=" + deltaCash +
                ", deltaCredit=" + deltaCredit +
                ", deltaMoneyFlow=" + deltaMoneyFlow +
                '}';
    }
}
