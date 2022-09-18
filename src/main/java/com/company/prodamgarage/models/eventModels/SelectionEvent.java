package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.DialogBuilder;
import com.company.prodamgarage.models.DialogFactory;
import com.company.prodamgarage.models.Event;
import com.company.prodamgarage.models.UserChanges;
import com.company.prodamgarage.models.dialogBuilders.GoodDialogBuilder;

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
        return new GoodDialogBuilder();
    }
}
