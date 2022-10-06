package com.company.prodamgarage.models.dialog.factory;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.eventModels.Event;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public interface DialogFactory {

    Dialog createNotificationDialog(String title, String mainText, UserChanges changes);

    Dialog createSelectionDialog(String title, String mainText, List<Pair<String, UserChanges>> changes);

    Dialog createPossibilitiesDialog(String title, List<Event> events);
}
