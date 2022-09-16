package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.DialogBuilder;
import com.company.prodamgarage.models.DialogFactory;
import com.company.prodamgarage.models.Event;

public class BadEvent extends Event {

    public String name;
    public String text;
    public int moneyBonus;


    public BadEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return null;
    }
}
