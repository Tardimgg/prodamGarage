package com.company.prodamgarage.controllers;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.RequiringTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class NotificationController implements RequiringTransition {


//    private PublishSubject<SceneType> reqTransition = PublishSubject.create();
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

    @FXML
    Tooltip tooltip3;

    private String titleSource;
    private String mainText;
    private UserChanges changes;

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

    public UserChanges getChanges() {
        return changes;
    }

    public void setChanges(UserChanges changes) {
        this.changes = changes;
    }

    @FXML
    public void initialize() {
        main_text.setText(mainText);
        title.setText(titleSource);
        reqTransition = PublishSubject.create();

        tooltip1.setShowDelay(Duration.seconds(0.1));
        tooltip2.setShowDelay(Duration.seconds(0.1));
        tooltip3.setShowDelay(Duration.seconds(0.1));
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(Pair.create(SceneType.BACK, null));
        reqTransition.onComplete();
    }
}
