package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.conditions.Conditions;
import com.company.prodamgarage.models.conditions.DefaultConditions;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import io.reactivex.Completable;

import java.io.Serializable;
import java.util.List;

public abstract class Event implements Serializable {

    protected DialogFactory dialogFactory;
    public Conditions conditions = new DefaultConditions();

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
