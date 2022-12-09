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
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

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
    Tooltip tooltip1;

    @FXML
    Tooltip tooltip2;

    @FXML
    Tooltip tooltip3;

    @FXML
    public void initialize() {
        reqTransition = PublishSubject.create();
        tooltip1.setShowDelay(Duration.seconds(0.1));
        tooltip2.setShowDelay(Duration.seconds(0.1));
        tooltip3.setShowDelay(Duration.seconds(0.1));
        tooltip1.setStyle("-fx-font-size: 20");
        tooltip2.setStyle("-fx-font-size: 20");
        tooltip3.setStyle("-fx-font-size: 20");
    }

    Object obj;

    @Override
    public void setData(Object obj) {
        this.obj = obj;

        reqTransition = PublishSubject.create();

//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//        service.schedule(() -> {
//            Platform.runLater(() -> {
//                back(null);
//            });
//        }, 5, TimeUnit.SECONDS);
    }

    public void showSimple() {
        reqTransition.onNext(Pair.create(SceneType.BUSINESS_POSSIBILITIES, obj));
        reqTransition.onComplete();
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }

    public void toHighEduc(ActionEvent actionEvent) {
        showSimple();
    }

    public void toSeminar(ActionEvent actionEvent) {
        showSimple();
    }

    public void toTimeManagement(ActionEvent actionEvent) {
        showSimple();
    }
}
