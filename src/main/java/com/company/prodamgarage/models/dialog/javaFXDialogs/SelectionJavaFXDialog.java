package com.company.prodamgarage.models.dialog.javaFXDialogs;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.controllers.NotificationController;
import com.company.prodamgarage.controllers.SelectionController;
import com.company.prodamgarage.controllers.possibilities.PossibilitiesController;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.user.UserChanges;
import javafx.scene.Parent;

import java.util.List;

public class SelectionJavaFXDialog implements Dialog {

    String title;
    String mainText;
    List<Pair<String, UserChanges>> changes;


    public SelectionJavaFXDialog(String title, String mainText, List<Pair<String, UserChanges>> changes) {
        this.title = title;
        this.mainText = mainText;
        this.changes = changes;
    }

    @Override
    public Object create() throws GameOver {
        Pair<Parent, ?> parentPair =  Resources.getParent(SceneType.SELECTION).blockingGet();

        SelectionController controller = ((SelectionController) parentPair.getValue());
        controller.setTitle(title);
        controller.setMainText(mainText);
        controller.setChanges(changes);
        controller.initialize();

        return parentPair;
    }
}
