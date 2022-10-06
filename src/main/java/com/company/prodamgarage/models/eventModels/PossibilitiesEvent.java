package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.PossibilityDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import io.reactivex.Completable;

public class PossibilityEvent extends Event {

    private volatile boolean isFullyLoaded = false;

    public PossibilityEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return new PossibilityDialogBuilder(this.dialogFactory);
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
