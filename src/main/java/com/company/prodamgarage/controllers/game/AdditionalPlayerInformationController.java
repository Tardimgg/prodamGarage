package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.WeatherModel;
import io.reactivex.Observer;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AdditionalPlayerInformationController {

    @FXML
    public Label clock;
    public Label weather;

    @FXML
    public void initialize() {


        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        });
        executor.scheduleAtFixedRate(() -> {
            Date date = new Date();
            SimpleDateFormat formatForTimeNow = new SimpleDateFormat("E hh:mm:ss");
            Platform.runLater(() -> clock.setText(formatForTimeNow.format(date)));

        }, 0, 1, TimeUnit.SECONDS);


        WeatherModel.getWeather()
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new DisposableSubscriber<>() {
                    @Override
                    public void onNext(String s) {
                        weather.setText("Температура в Москве: " + s);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private final PublishSubject<ActionEvent> nextStep = PublishSubject.create();

    public void subscribeToNextStep(Observer<ActionEvent> obs) {
        nextStep.subscribe(obs);
    }

    public void nextStep(ActionEvent actionEvent) {
        nextStep.onNext(actionEvent);
    }
}
