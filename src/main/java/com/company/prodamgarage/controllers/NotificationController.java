package com.company.prodamgarage.controllers;

import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.models.user.UserChanges;
import io.reactivex.Observer;
import io.reactivex.subjects.PublishSubject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class NotificationController implements RequestTransition {

    private PublishSubject<SceneType> reqTransition = PublishSubject.create();


    @Override
    public void subscribe(Observer<SceneType> obs) {
        reqTransition.subscribe(obs);
    }

    public TextArea main_text;
    private String title;
    private String mainText;
    private UserChanges changes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        main_text.setText(title);
        reqTransition = PublishSubject.create();
    }


    public void back(ActionEvent actionEvent) {
        reqTransition.onNext(SceneType.BACK);
        reqTransition.onComplete();
    }
}
