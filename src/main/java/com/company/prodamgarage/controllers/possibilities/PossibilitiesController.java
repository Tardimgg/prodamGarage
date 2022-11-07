package com.company.prodamgarage.controllers.possibilities;

import com.company.prodamgarage.*;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import io.reactivex.Observer;
import io.reactivex.internal.observers.BiConsumerSingleObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.List;

public class PossibilitiesController implements RequiringTransition {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    public Label title;

    @FXML
    public StackPane rootPossibilities;

    private String titleSource;
    List<Pair<PossibilityType, List<Possibility>>> possibilities;

    public void setTitle(String title) {
        this.titleSource = title;
    }

    public String getTitle() {
        return titleSource;
    }

    public void setPossibilities(List<Pair<PossibilityType, List<Possibility>>> possibilities) {
        this.possibilities = possibilities;
    }

    public List<Pair<PossibilityType, List<Possibility>>> getPossibilities() {
        return possibilities;
    }


    @FXML
    public void initialize() {
//        title.setText(titleSource);
        reqTransition = PublishSubject.create();
    }

    public void initView() {
        reqTransition = PublishSubject.create();
        showMenu();
    }

    private void showMenu() {
        Resources.getParent(SceneType.POSSIBILITIES_MENU)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {

                    rootPossibilities.getChildren().clear();

                    ((PossibilitiesMenuController) parentPair.value).setPossibilities(possibilities);
                    ((PossibilitiesMenuController) parentPair.value).setTitle(titleSource);
                    ((PossibilitiesMenuController) parentPair.value).initView();

                    rootPossibilities.getChildren().setAll(parentPair.key);

                    if (parentPair.getValue() instanceof RequiringTransition controller) {
                        controller.subscribe(new DefaultObserver<>() {

                            @Override
                            public void onNext(Pair<SceneType, Object> sceneInfo) {
                                showDialog(sceneInfo);
                            }

                        });
                    } else {
                        System.out.println("warning: " + parentPair.getValue().getClass() +
                                " not implement RequestTransition");
                    }
                }));
    }

    private void showDialog(Pair<SceneType, Object> sceneInfo) {
        Resources.getParent(sceneInfo.key)
                .subscribeOn(Schedulers.computation())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(new BiConsumerSingleObserver<>((parentPair, throwable) -> {
                    if (parentPair == null || throwable != null) {
                        back(null);
                    }

                    rootPossibilities.getChildren().clear();
                    rootPossibilities.getChildren().setAll(parentPair.key);

                    if (parentPair.getValue() instanceof RequiringData infoController) {
                        infoController.setData(sceneInfo.getValue());
                    }

                    if (parentPair.getValue() instanceof RequiringTransition controller) {
                        controller.subscribe(new DefaultObserver<>() {
                            @Override
                            public void onNext(Pair<SceneType, Object> sceneInfo) {
                                if (sceneInfo.key == SceneType.BACK) {
                                    back(null);
                                } else {
                                    showDialog(sceneInfo);
                                }
                            }
                        });
                    } else {
                        System.out.println("warning: " + parentPair.getValue().getClass() +
                                " not implement RequestTransition");
                    }
                }));
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
