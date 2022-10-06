package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import io.reactivex.Completable;

public abstract class Event {

    protected DialogFactory dialogFactory;

    public Event(DialogFactory dialogFactory) {
        this.dialogFactory = dialogFactory;
    }

    public abstract DialogBuilder dialogBuilder();

    public boolean isFullyLoaded() {
        return true;
    }

    public Completable load() {
        return Completable.complete();
    }
}
