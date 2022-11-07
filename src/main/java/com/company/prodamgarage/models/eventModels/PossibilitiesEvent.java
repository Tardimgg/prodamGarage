package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.PossibilitiesDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import io.reactivex.Completable;

import java.util.List;

public class PossibilitiesEvent extends Event {

    private volatile boolean isFullyLoaded = false;

    List<Possibility> apartmentPossibilities;
    List<Possibility> businessPossibilities;
    List<Possibility> educationPossibilities;

    public PossibilitiesEvent(DialogFactory dialogFactory) {
        super(dialogFactory);
    }

    @Override
    public DialogBuilder dialogBuilder() {
        return new PossibilitiesDialogBuilder(this.dialogFactory)
                .setPossibilities(PossibilityType.APARTMENT, apartmentPossibilities)
                .setPossibilities(PossibilityType.BUSINESS, businessPossibilities)
                .setPossibilities(PossibilityType.EDUCATION, educationPossibilities);
    }

    @Override
    public boolean isFullyLoaded() {
        return isFullyLoaded;
    }

    @Override
    public Completable load() {
        return Completable.create(completableEmitter -> {

            apartmentPossibilities = PossibilitiesLoader.getPossibilitiesRepository()
                    .blockingGet()
                    .getPossibilities(PossibilityType.APARTMENT);

            businessPossibilities = PossibilitiesLoader.getPossibilitiesRepository()
                    .blockingGet()
                    .getPossibilities(PossibilityType.BUSINESS);

            educationPossibilities = PossibilitiesLoader.getPossibilitiesRepository()
                    .blockingGet()
                    .getPossibilities(PossibilityType.EDUCATION);
            
            isFullyLoaded = true;
            completableEmitter.onComplete();
        });
    }
}
