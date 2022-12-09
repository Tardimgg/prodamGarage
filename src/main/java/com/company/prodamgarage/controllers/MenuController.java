package com.company.prodamgarage.controllers;

import com.company.prodamgarage.*;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.user.DefaultUserChanges;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Observer;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuController implements RequiringTransition {

    private final PublishSubject<Pair<SceneType, Object>> reqTransition = PublishSubject.create();
    public Button newGameBtn;
    public StackPane rootPane;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);

    }

//    @FXML
//    private Label game_name;

    @FXML
    private Button settings_btn;

    @FXML
    protected void showGame(ActionEvent e) {
        reqTransition.onNext(Pair.create(SceneType.GAME, null));
        reqTransition.onComplete();
    }


    public void newGame(ActionEvent actionEvent) {
        newGameBtn.setDisable(true);
        User.newInstance()
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((user, throwable) -> {
                    reqTransition.onNext(Pair.create(SceneType.GAME, null));
                    reqTransition.onComplete();
                    newGameBtn.setDisable(false);
                }));
    }

    public void openHistory(ActionEvent actionEvent) {
        Resources.getParent(SceneType.NOTIFICATION)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    if (parentPair != null) {
                        ((NotificationController) parentPair.getValue()).setTitle("История");
                        ((NotificationController) parentPair.getValue()).setMainText("Пусто");
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


    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }


}