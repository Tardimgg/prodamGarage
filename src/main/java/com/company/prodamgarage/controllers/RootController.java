package com.company.prodamgarage.controllers;

import com.company.prodamgarage.*;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class RootController {

    @FXML
    private StackPane rootView;

    @FXML
    public void initialize() {

    }

    public void setView(Pair<SceneType, Object> sceneInfo) {
        Resources.getParent(sceneInfo.key)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    rootView.getChildren().clear();
                    rootView.getChildren().setAll(parentPair.key);

                    if (parentPair.getValue() instanceof RequiringData infoController) {
                        infoController.setData(sceneInfo.getValue());
                    }

                    if (parentPair.getValue() instanceof RequiringTransition controller) {
                        controller.subscribe(new DefaultObserver<>() {
                            @Override
                            public void onNext(Pair<SceneType, Object> sceneInfo) {
                                setView(sceneInfo); // !!! рекурсия !!! (не факт)
                            }
                        });
                    } else {
                        System.out.println("warning: " + parentPair.getClass() + " not implement RequestTransition");
                    }
                }));
    }
}
