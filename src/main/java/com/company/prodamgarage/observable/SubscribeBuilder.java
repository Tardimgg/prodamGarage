package com.company.prodamgarage.observable;

import com.company.prodamgarage.DefaultObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.subjects.BehaviorSubject;

public class SubscribeBuilder<T> {

    BehaviorSubject<T> subject;

    public SubscribeBuilder(BehaviorSubject<T> subject) {
        this.subject = subject;
    }

    public Observable<T> subscribeOn(Scheduler scheduler) {
        return this.subject.subscribeOn(scheduler);
    }

    public void subscribe(DefaultObserver<T> obs) {
        this.subject.subscribe(obs);
    }
}
