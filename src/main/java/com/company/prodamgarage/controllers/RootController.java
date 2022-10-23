package com.company.prodamgarage.controllers;

import com.company.prodamgarage.DefaultObserver;
import com.company.prodamgarage.Pair;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.SceneType;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class RootController {

    @FXML
    private StackPane rootView;

    @FXML
    public void initialize() {

    }

    public void setView(SceneType sceneType) {
        Resources.getParent(sceneType)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    rootView.getChildren().clear();
                    rootView.getChildren().setAll(parentPair.key);

                    if (parentPair.getValue() instanceof RequestTransition controller) {
                        controller.subscribe(new DefaultObserver<>() {
                            @Override
                            public void onNext(SceneType sceneType) {
                                setView(sceneType); // !!! рекурсия !!! (не факт)
                            }
                        });
                    } else {
                        System.out.println("warning: " + parentPair.getClass() + " not implement RequestTransition");
                    }
                }));
    }
}
