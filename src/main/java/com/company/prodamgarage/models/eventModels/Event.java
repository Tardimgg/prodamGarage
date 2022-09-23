package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;

public abstract class Event {

    protected DialogFactory dialogFactory;

    public Event(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    public abstract DialogBuilder dialogBuilder();
}
