package com.company.prodamgarage.models.dialog.javaFXDialogs;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.controllers.NotificationController;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.UserChanges;
import javafx.scene.Parent;

public class NotificationJavaFXDialog implements Dialog {

    String title;
    String mainText;
    UserChanges changes;

    public NotificationJavaFXDialog(String title, String mainText, UserChanges changes) {
        this.title = title;
        this.mainText = mainText;
        this.changes = changes;
    }


    @Override
    public Pair<Parent, ?> show() {
        Pair<Parent, ?> parentPair =  Resources.getParent(SceneType.NOTIFICATION).blockingGet();
        ((NotificationController) parentPair.getValue()).setTitle(title);
        ((NotificationController) parentPair.getValue()).setMainText(mainText);
        ((NotificationController) parentPair.getValue()).setChanges(changes);
        ((NotificationController) parentPair.getValue()).initialize();

        return parentPair;
    }

}
