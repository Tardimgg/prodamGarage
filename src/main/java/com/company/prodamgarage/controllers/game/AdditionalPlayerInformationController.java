package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.SceneType;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;

public class AdditionalPlayerInformationController {

    private final PublishSubject<ActionEvent> nextStep = PublishSubject.create();

    public void subscribeToNextStep(Observer<ActionEvent> obs) {
        nextStep.subscribe(obs);
    }

    public void nextStep(ActionEvent actionEvent) {
        nextStep.onNext(actionEvent);
    }
}
