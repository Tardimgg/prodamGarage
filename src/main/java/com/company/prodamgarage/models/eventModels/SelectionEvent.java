package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.user.UserChanges;
import com.company.prodamgarage.models.dialogBuilders.SelectionDialogBuilder;

public class SelectionEvent extends Event {

    public String name;

    public String text;
    public int moneyBonus;
    public UserChanges userChanges;


    public SelectionEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }


    @Override
    public DialogBuilder dialogBuilder() {
        return new SelectionDialogBuilder();
    }
}
