package com.company.prodamgarage.models;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;

import java.io.IOException;

public class UserChanges {

    private int deltaAge;
    private int deltaCash;
    private int deltaCredit;
    private int deltaMoneyFlow;

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

                } catch (IOException e) {
                    e.printStackTrace();
                    completableObserver.onError(e);
                }
            }
        };
    }
}
