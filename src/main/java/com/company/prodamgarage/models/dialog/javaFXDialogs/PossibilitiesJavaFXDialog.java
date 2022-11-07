package com.company.prodamgarage.models.dialog.javaFXDialogs;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.Resources;
import com.company.prodamgarage.SceneType;
import com.company.prodamgarage.controllers.NotificationController;
import com.company.prodamgarage.controllers.possibilities.PossibilitiesController;
import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import javafx.scene.Parent;

import java.util.List;

public class PossibilitiesJavaFXDialog implements Dialog {

    List<Pair<PossibilityType, List<Possibility>>> possibilities;
    String title;

    public PossibilitiesJavaFXDialog(String title, List<Pair<PossibilityType, List<Possibility>>> possibilities) {
        this.title = title;
        this.possibilities = possibilities;
    }

    @Override
    public Object create() throws GameOver {
        Pair<Parent, ?> parentPair =  Resources.getParent(SceneType.POSSIBILITIES).blockingGet();
        PossibilitiesController controller = ((PossibilitiesController) parentPair.getValue());
        controller.setTitle(title);
        controller.setPossibilities(possibilities);
        controller.initView();

        return parentPair;
    }
}
