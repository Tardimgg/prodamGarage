package com.company.prodamgarage.models.dialog.factory;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.consoleDialogs.NotificationConsoleDialog;
import com.company.prodamgarage.models.dialog.consoleDialogs.PossibilitiesConsoleDialog;
import com.company.prodamgarage.models.dialog.consoleDialogs.SelectionConsoleDialog;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class ConsoleDialogFactory implements DialogFactory{

    @Override
    public Dialog createNotificationDialog(String title, String mainText, UserChanges changes) {
        return new NotificationConsoleDialog(title, mainText, changes);
    }

    @Override
    public Dialog createSelectionDialog(String title, String mainText, List<Pair<String, UserChanges>> changes) {
        return new SelectionConsoleDialog(title, mainText, changes);
    }

    @Override
    public Dialog createPossibilitiesDialog(String title, List<Possibility> apartmentPossibilities,
                                            List<Possibility> businessPossibilities) {
        return new PossibilitiesConsoleDialog(title, apartmentPossibilities, businessPossibilities);
    }

}
