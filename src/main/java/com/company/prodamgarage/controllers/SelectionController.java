package com.company.prodamgarage.controllers;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Observer;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

import java.util.List;

public class SelectionController implements RequiringTransition {


    public Button firstBtn;
    public Button secondBtn;
    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    public Label title;

    @FXML
    public TextArea main_text;
    @FXML
    Tooltip tooltip1;

    @FXML
    Tooltip tooltip2;

//    @FXML
//    Tooltip tooltip3;

    private String titleSource;
    private String mainText;
    private List<Pair<String, UserChanges>> changes;

    public String getTitle() {
        return titleSource;
    }

    public void setTitle(String title) {
        this.titleSource = title;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public List<Pair<String, UserChanges>>  getChanges() {
        return changes;
    }

    public void setChanges(List<Pair<String, UserChanges>> changes) {
        this.changes = changes;
    }

    @FXML
    public void initialize() {
        main_text.setText(mainText);
        title.setText(titleSource);


        if (changes != null) {
            if (changes.size() > 0) {
                firstBtn.setOnAction(actionEvent -> {
                    changes.get(0).getValue().apply()
                            .subscribeOn(Schedulers.computation())
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(new DisposableCompletableObs() {
                                @Override
                                public void onComplete() {
                                    back(null);
                                }
                            });
                });
                tooltip1.setText(changes.get(0).key);
            }
            if (changes.size() > 1) {
                secondBtn.setOnAction(actionEvent -> {
                    changes.get(1).getValue().apply()
                            .subscribeOn(Schedulers.computation())
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(new DisposableCompletableObs() {
                                @Override
                                public void onComplete() {
                                    back(null);
                                }
                            });
                });
                tooltip2.setText(changes.get(1).key);
            }
        }


        reqTransition = PublishSubject.create();

        tooltip1.setShowDelay(Duration.seconds(0.1));
        tooltip2.setShowDelay(Duration.seconds(0.1));
//        tooltip3.setShowDelay(Duration.seconds(0.1));
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }

    private static abstract class DisposableCompletableObs extends DisposableCompletableObserver {
        @Override
        public void onError(Throwable throwable) {

        }
    }
}
