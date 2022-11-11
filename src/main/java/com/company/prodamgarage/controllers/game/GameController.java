package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.*;
import com.company.prodamgarage.models.Game;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import io.reactivex.*;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController implements RequiringTransition {

    private final PublishSubject<Pair<SceneType, Object>> reqTransition = PublishSubject.create();
    public StackPane rootPane;
    private final Queue<Dialog> deferredDialogs = new ConcurrentLinkedQueue<>();


    @FXML
    public BorderPane additional_info;
    public ImageView player;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    Button nextStep;

    private Map map;

    @FXML
    public void initialize() {

        nextStep = (Button) additional_info.lookup("#nextStepBtn");
        nextStep.setOnAction(this::nextStep);

        map = new Map();
        movePlayer();
    }

    private void movePlayer() {
        Pair<Integer, Integer> pos = map.getNextPos(rootPane.heightProperty().doubleValue(), rootPane.widthProperty().doubleValue());
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), player);
        transition.setToX(pos.key);
        transition.setToY(pos.value);
        transition.play();
    }

    private Completable showDialog(Dialog dialog) {
        return Completable.create(completableEmitter -> {
            Pair<Parent, ?> parentPair = null;
            try {
                parentPair = (Pair<Parent, ?>) dialog.create();
            } catch (GameOver ex) {
                ex.printStackTrace();
            }

            if (parentPair != null) { // TEMP CODE!!!!!!!!!!!!!!!

                parentPair.key.maxHeight(100);
                parentPair.key.maxWidth(100);
//                parentPair.key.setEffect(new DropShadow(20d, 0d, +2d, Color.BLACK));

                rootPane.getChildren().addAll(parentPair.key);

                if (parentPair.value instanceof RequiringTransition controller) {

                    Pair<Parent, ?> finalParentPair = parentPair;

                    controller.subscribe(new DefaultObserver<>() {
                        @Override
                        public void onNext(Pair<SceneType, Object> sceneInfo) {
                            if (sceneInfo.key == SceneType.BACK) {

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

                showDialog(dialog).subscribeOn(JavaFxScheduler.platform()).blockingGet();

            }
            completableEmitter.onComplete();
        });
    }



    final AtomicBoolean displayDialogComplete = new AtomicBoolean(true);
    final AtomicBoolean allDialogsLoaded = new AtomicBoolean(false);

    @FXML
    protected void nextStep(ActionEvent e) {
        synchronized (GameController.class) {
            if (nextStep.isDisabled()) {
                return;
            }
            nextStep.setDisable(true);
        }
        movePlayer();
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
                                                                    nextStep.setDisable(false);

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
                                        .subscribeOn(Schedulers.computation())
                                        .subscribe(new DisposableCompletableObserver() {
                                            @Override
                                            public void onComplete() {
                                                nextStep.setDisable(false);

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