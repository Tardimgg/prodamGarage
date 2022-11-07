package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.dialog.dialogBuilders.EducationDialogBuilder;
import com.company.prodamgarage.models.dialog.dialogBuilders.PossibilitiesDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import io.reactivex.Completable;

import java.util.List;

public class EducationEvent extends Event { // not used

    private volatile boolean isFullyLoaded = false;


    public EducationEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return new EducationDialogBuilder(this.dialogFactory);
    }

    @Override
    public boolean isFullyLoaded() {
        return isFullyLoaded;
    }

    @Override
    public Completable load() {
        return Completable.create(completableEmitter -> {

            // necessary to add reading
            isFullyLoaded = true;
        });
    }

}
