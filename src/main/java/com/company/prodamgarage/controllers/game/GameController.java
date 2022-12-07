package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.*;
import com.company.prodamgarage.controllers.NotificationController;
import com.company.prodamgarage.models.Game;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.DefaultUserChanges;
import io.reactivex.*;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController implements RequiringTransition, RequiringWindowSize {

    private final PublishSubject<Pair<SceneType, Object>> reqTransition = PublishSubject.create();
    public StackPane rootPane;
//    private final Queue<Dialog> deferredDialogs = new ConcurrentLinkedQueue<>();
    private final Queue<Dialog> deferredDialogs = new PriorityBlockingQueue<>(1, Comparator.comparingInt(o -> o.importance));


    @FXML
    public BorderPane additional_info;
    public ImageView player;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    Button nextStep;

    private volatile Map map;

    @Override
    public void setSize(double height, double width) {
        movePlayer(height, width, Duration.millis(50));
        player.setVisible(true);
    }

    @FXML
    public void initialize() {

        nextStep = (Button) additional_info.lookup("#nextStepBtn");
        nextStep.setOnAction(this::nextStep);

        map = Map.newInstance().blockingGet();
    }

    private void movePlayer(double height, double width, Duration duration) {
        Pair<Integer, Integer> pos = map.getNextPos(height, width);
        TranslateTransition transition = new TranslateTransition(duration, player);
        transition.setToX(pos.key);
        transition.setToY(pos.value);
        transition.play();
    }

    private void movePlayer() {
        movePlayer(rootPane.heightProperty().doubleValue(),
                rootPane.widthProperty().doubleValue(),
                Duration.millis(500)
        );
    }

    private void showGameOverDialog(GameOver gameOver) {
        Resources.getParent(SceneType.NOTIFICATION)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    if (parentPair != null) {
                        ((NotificationController) parentPair.getValue()).setTitle("Игра пройдена");
                        ((NotificationController) parentPair.getValue()).setMainText(gameOver.getMessage());
                        ((NotificationController) parentPair.getValue()).setChanges(new DefaultUserChanges());
                        ((NotificationController) parentPair.getValue()).initialize();
                        rootPane.getChildren().addAll(parentPair.key);

                        if (parentPair.value instanceof RequiringTransition controller) {

                            controller.subscribe(new DefaultObserver<>() {
                                @Override
                                public void onNext(Pair<SceneType, Object> sceneInfo) {
                                    if (sceneInfo.key == SceneType.BACK) {

                                        rootPane.getChildren().remove(parentPair.key);
                                    }
                                }
                            });
                        }
                    }

                }));
    }

    private Completable showDialog(Dialog dialog) {
        return Completable.create(completableEmitter -> {
            Pair<Parent, ?> parentPair;
            try {
                parentPair = (Pair<Parent, ?>) dialog.create();
            } catch (GameOver ex) {
                showGameOverDialog(ex);
                return;
//                ex.printStackTrace();
            }
            if (parentPair == null) {
                completableEmitter.onError(new NullPointerException("dialog is null"));
                return;
            }

//            if (parentPair != null) { // TEMP CODE!!!!!!!!!!!!!!!

//                parentPair.key.maxHeight(100);
//                parentPair.key.maxWidth(100);
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
//            } else {
//                completableEmitter.onComplete();
//            }
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

    final AtomicBoolean gameOver = new AtomicBoolean(false);

    @FXML
    protected void nextStep(ActionEvent e) {
        synchronized (GameController.class) {
            if (nextStep.isDisabled()) {
                return;
            }
            nextStep.setDisable(true);
        }
        System.out.println("click");
        movePlayer();
        allDialogsLoaded.set(false);

        Game.getInstance().getNext()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(new DisposableSubscriber<>() {
                    @Override
                    public void onNext(Dialog dialog) {
                        System.out.println(dialog.getClass().toString() + " " + deferredDialogs.size());

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

                                                                    synchronized (gameOver) {
                                                                        if (!gameOver.get()) {
                                                                            nextStep.setDisable(false);
                                                                        }
                                                                    }

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
                        if (throwable instanceof GameOver) {
                            synchronized (gameOver) {
                                nextStep.setDisable(true);
                                gameOver.set(true);
                            }
                            onComplete();
                        }
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
                                                synchronized (gameOver) {
                                                    if (!gameOver.get()) {
                                                        nextStep.setDisable(false);
                                                    }
                                                }

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