package com.company.prodamgarage.models.user;

import com.company.prodamgarage.models.user.User;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

import java.io.IOException;

public class UserChanges {

    public int deltaAge;
    public int deltaCash;
    public int deltaCredit;
    public int deltaMoneyFlow;

    public Completable apply() {
        return new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver completableObserver) {
                try {
                    User user = User.getInstance().blockingGet();
                    user.setAge(user.getAge() + deltaAge);
                    user.setAge(user.getCash() + deltaCash);
                    user.setAge(user.getCredit() + deltaCredit);
                    user.setAge(user.getMoneyFlow() + deltaMoneyFlow);

                    completableObserver.onComplete();

                } catch (RuntimeException e) {
                    e.printStackTrace();
                    completableObserver.onError(e);
                }
            }
        };
    }
}
