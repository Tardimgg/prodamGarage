package com.company.prodamgarage.models.dialog.factory;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import com.company.prodamgarage.models.user.UserChanges;

import java.io.Serializable;
import java.util.List;

public interface DialogFactory extends Serializable {

    Dialog createNotificationDialog(String title, String mainText, UserChanges changes);

    Dialog createSelectionDialog(String title, String mainText, List<Pair<String, UserChanges>> changes);

    Dialog createPossibilitiesDialog(String title,
                                     List<Pair<PossibilityType, List<Possibility>>> possibilities);
}
