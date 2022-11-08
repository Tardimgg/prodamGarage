package com.company.prodamgarage;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DefaultObserver<T> implements Observer<T> {
    Disposable disposable;

    @Override
    public void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public void onError(Throwable throwable) {
        onComplete();
    }

    @Override
    public void onComplete() {
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onNext(T t) {

    }
}
