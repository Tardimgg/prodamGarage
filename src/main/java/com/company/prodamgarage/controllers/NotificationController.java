package com.company.prodamgarage.controllers;

import com.company.prodamgarage.RequestTransition;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class NotificationController implements RequestTransition {


//    private PublishSubject<SceneType> reqTransition = PublishSubject.create();
    private PublishSubject<SceneType> reqTransition;

    @Override
    public void subscribe(Observer<SceneType> obs) {
        reqTransition.subscribe(obs);
    }


    public Label title;
    public TextArea main_text;

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
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(SceneType.BACK);
        reqTransition.onComplete();
    }
}
