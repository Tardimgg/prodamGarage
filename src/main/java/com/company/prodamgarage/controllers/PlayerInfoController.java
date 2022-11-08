package com.company.prodamgarage.controllers;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.models.user.User;
import com.company.prodamgarage.observable.SubscribeBuilder;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PlayerInfoController {

    @FXML
    private Label money;

    @FXML
    private Label time;

    @FXML
    private Label moneyFlow;

    private <T> void bind(Label label, SubscribeBuilder<T> subscribeBuilder) {
        subscribeBuilder.subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new DefaultObserver<>() {
                    @Override
                    public void onNext(T o) {
                        label.setText(o.toString());
                    }
                });
    }

    @FXML
    public void initialize() {
        User.getInstance()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new BiConsumerSingleObserver<>((user, throwable) -> {
                    if (user != null && throwable == null) {

                        bind(money, user.subscribeAge());
                        bind(moneyFlow, user.subscribeMoneyFlow());
                        bind(time, user.subscribeCurrentTime());

                    } else {
                        throwable.printStackTrace();
                    }
                }));
    }

}
