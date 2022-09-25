package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.models.dialog.dialogBuilders.SelectionDialogBuilder;

import java.util.List;

public class SelectionEvent extends Event {

    public String title;
    public String mainText;
    public List<Pair<String, UserChanges>> userChanges;


    public SelectionEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public void dialogBuilder() {

    }
}
