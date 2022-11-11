package com.company.prodamgarage.observable;

import io.reactivex.subjects.BehaviorSubject;

import java.io.Serializable;

public class SimpleObservable<T> implements Serializable {
    private T value;
    private transient volatile BehaviorSubject<T> subject = null;

    private void checkAndInitPublisher() {
        if (subject == null) {
            subject = BehaviorSubject.create();
            subject.onNext(value);
        }
    }

    public SimpleObservable(T value) {
        this.value = value;
    }

    public void set(T value) {
        checkAndInitPublisher();
        this.value = value;
        subject.onNext(this.value);
    }

    public T get() {
        return value;
    }

    public void changed() {
        checkAndInitPublisher();
        subject.onNext(value);
    }

    public SubscribeBuilder<T> subscribe() {
        checkAndInitPublisher();

        return new SubscribeBuilder<>(subject);
    }

    @Override
    protected void finalize() throws Throwable {
        if (subject != null) {
            subject.onComplete();
        }
        super.finalize();
    }
}
