package com.company.prodamgarage.models;
import DialogFactory;
import DialogBuilder;

public abstract class Event {
    DialogFactory dialogFactory;
    public Event(DialogFactory dialogFactory)
    {
        this.dialogFactory = dialogFactory;
    }

    public abstract DialogBuilder dialogBuilder();
}
