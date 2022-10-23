package com.company.prodamgarage.controllers;

import com.company.prodamgarage.*;
import com.company.prodamgarage.models.Game;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import io.reactivex.*;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController implements RequestTransition {

    private final PublishSubject<SceneType> reqTransition = PublishSubject.create();
    public StackPane rootPane;
    private final Queue<Dialog> deferredDialogs = new ConcurrentLinkedQueue<>();


    @Override
    public void subscribe(Observer<SceneType> obs) {
        reqTransition.subscribe(obs);

    }

    @FXML
    public void initialize() {

    }

    private Completable showDialog(Dialog dialog) {
        return Completable.create(completableEmitter -> {
            Pair<Parent, ?> parentPair = null;
            try {
                parentPair = (Pair<Parent, ?>) dialog.show();
            } catch (GameOver ex) {
                ex.printStackTrace();
            }

            if (parentPair != null) { // TEMP CODE!!!!!!!!!!!!!!!

                parentPair.key.maxHeight(100);
                parentPair.key.maxWidth(100);

                rootPane.getChildren().addAll(parentPair.key);

                if (parentPair.value instanceof RequestTransition controller) {

                    Pair<Parent, ?> finalParentPair = parentPair;

                    controller.subscribe(new DefaultObserver<>() {
                        @Override
                        public void onNext(SceneType sceneType) {
                            if (sceneType == SceneType.BACK) {

                                rootPane.getChildren().remove(finalParentPair.key);
                                completableEmitter.onComplete();
                            } else {
                                completableEmitter.onError(null);
                            }
                        }
                    });
                }
            } else {
                completableEmitter.onComplete();
            }
        });
    }


    private synchronized Completable startShowingDialogs() {
        return Completable.create(completableEmitter -> {
            Dialog dialog;
            while ((dialog = deferredDialogs.poll()) != null) {

                showDialog(dialog)
                        .subscribeOn(JavaFxScheduler.platform()).blockingGet();

            }
            completableEmitter.onComplete();
        });
    }



    final AtomicBoolean displayDialogComplete = new AtomicBoolean(true);
    final AtomicBoolean allDialogsLoaded = new AtomicBoolean(false);

    @FXML
    protected void nextStep(ActionEvent e) {
        allDialogsLoaded.set(false);

        Game.getInstance().getNext()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new DisposableSubscriber<>() {
                    @Override
                    public void onNext(Dialog dialog) {
                        deferredDialogs.add(dialog);

                        if (displayDialogComplete.compareAndSet(true, false)) {
                            showDialog(deferredDialogs.poll())
                                    .subscribeOn(JavaFxScheduler.platform())
                                    .observeOn(JavaFxScheduler.platform())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onSubscribe(Disposable disposable) {
                                        }

                                        @Override
                                        public void onComplete() {
                                            synchronized (displayDialogComplete) {
                                                if (allDialogsLoaded.get()) {
                                                    startShowingDialogs()
                                                            .subscribeOn(Schedulers.computation())
                                                            .subscribe(new DisposableCompletableObserver() {
                                                                @Override
                                                                public void onComplete() {

                                                                }

                                                                @Override
                                                                public void onError(Throwable throwable) {

                                                                }
                                                            });
                                                }
                                                displayDialogComplete.set(true);
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable throwable) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {
                        synchronized (displayDialogComplete) {
                            if (displayDialogComplete.get()) {
                                startShowingDialogs()
                                        .subscribeOn(JavaFxScheduler.platform())
                                        .subscribe(new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {

                                            }

                                            @Override
                                            public void onError(Throwable throwable) {

                                            }
                                        });
                            }
                            allDialogsLoaded.set(true);
                        }
                    }
                });
    }
}