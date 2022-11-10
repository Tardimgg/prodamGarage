package com.company.prodamgarage.models.user;

import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;

import java.io.IOException;
import java.util.List;

public class UserChanges {

    public int deltaAge;
    public int deltaCash;
    public int deltaCredit;
    public int deltaMoneyFlow;
    public List<Event> deferredEvents;

    public Completable apply() {
        return Completable.create(completableEmitter -> {
            try {
                User user = User.getInstance().blockingGet();
                user.setAge(user.getAge() + deltaAge);
                user.setCash(user.getCash() + deltaCash);
                user.setCredit(user.getCredit() + deltaCredit);
                user.setMoneyFlow(user.getMoneyFlow() + deltaMoneyFlow);

                if (deferredEvents != null) {
                    user.addDeferredEvents(deferredEvents);
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
