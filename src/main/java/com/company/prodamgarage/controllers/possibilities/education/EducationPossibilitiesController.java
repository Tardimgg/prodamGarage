package com.company.prodamgarage.controllers.possibilities.education;

import com.company.prodamgarage.*;
import com.company.prodamgarage.controllers.possibilities.business.BusinessPossibilitiesUnitController;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EducationPossibilitiesController implements RequiringTransition, RequiringData {

    private PublishSubject<Pair<SceneType, Object>> reqTransition;

    @Override
    public void subscribe(Observer<Pair<SceneType, Object>> obs) {
        reqTransition.subscribe(obs);
    }

    public ListView educationType;

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
    }

    @Override
    public void setData(Object obj) {

        reqTransition = PublishSubject.create();

        /*ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            Platform.runLater(() -> {
                back(null);
            });
        }, 2, TimeUnit.SECONDS);*/
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
