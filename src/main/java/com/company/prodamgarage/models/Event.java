package com.company.prodamgarage.models;

public abstract class Event {
    private DialogFactory dialogFactory;
    public Event(DialogFactory dialogFactory)
    {
        this.dialogFactory = dialogFactory;
    }

    public abstract DialogBuilder dialogBuilder();
}
