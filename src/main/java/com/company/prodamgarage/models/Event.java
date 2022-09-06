package com.company.prodamgarage.models;

public abstract class Event {
    DialogFactory dialogFactory;
    public Event(DialogFactory dialogFactory)
    {
        this.dialogFactory = dialogFactory;
    }

    public abstract DialogBuilder dialogBuilder();
}
