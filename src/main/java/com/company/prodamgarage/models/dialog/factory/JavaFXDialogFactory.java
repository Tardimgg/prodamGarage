package com.company.prodamgarage.models.dialog.factory;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.javaFXDialogs.JavaFXDialog;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class JavaFXDialogFactory implements DialogFactory {

    @Override
    public Dialog createNotificationDialog(String title, String mainText, UserChanges changes) {
        return new JavaFXDialog();
    }

    @Override
    public void createNotificationDialogTest(String title, String mainText, UserChanges changes) {

    }

    @Override
    public Dialog createSelectionDialog(String title, String mainText, List<Pair<String, UserChanges>> changes) {
        return null;
    }

    @Override
    public Dialog createPurchaseDialog(String title, List<Event> events) {
        return null;
    }

}
