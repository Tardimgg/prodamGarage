package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.PossibilitiesDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import io.reactivex.Completable;

public class PossibilitiesEvent extends Event {

    private volatile boolean isFullyLoaded = false;

    public PossibilitiesEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return new PossibilitiesDialogBuilder(this.dialogFactory);
    }

    @Override
    public boolean isFullyLoaded() {
        return isFullyLoaded;
    }

    @Override
    public Completable load() {
        return Completable.create(completableEmitter -> {

            // necessary to add reading of all types of possibility
            var res = PossibilitiesLoader.getPossibilitiesRepository().blockingGet().getPossibilities();
            isFullyLoaded = true;
        });
    }
}
