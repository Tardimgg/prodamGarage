package com.company.prodamgarage.controllers.possibilities.business;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
public class BusinessPossibilitiesUnitController implements RequiringTransition {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    TextArea main_text;

    @FXML
    Tooltip tooltip1;

    @FXML
    Tooltip tooltip2;

    @FXML
    Tooltip tooltip3;

//    Tooltip tooltip = new Tooltip("A tooltip");
//    tooltip.setShowDelay(Duration.seconds(3));
    Possibility possibility;

    public void setDate(Possibility possibility) {
        this.possibility = possibility;
        main_text.setText(possibility.description);
    }

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
        tooltip1.setShowDelay(Duration.seconds(0.1));
        tooltip2.setShowDelay(Duration.seconds(0.1));
        tooltip3.setShowDelay(Duration.seconds(0.1));
    }


    public void apply(ActionEvent actionEvent) {
        possibility.userChanges.apply().subscribeOn(Schedulers.computation()).subscribe();
        back(actionEvent);
    }

    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
