package com.company.prodamgarage.controllers.possibilities.business;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BusinessPossibilitiesUnitController implements RequiringTransition {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    @FXML
    TextArea main_text;

    Possibility possibility;

    public void setDate(Possibility possibility) {
        this.possibility = possibility;
        main_text.setText(possibility.description);
    }

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
    }


    public void apply(ActionEvent actionEvent) {
        possibility.userChanges.apply().subscribe();
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
