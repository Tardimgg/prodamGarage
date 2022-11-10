package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.dialog.dialogBuilders.PossibilitiesDialogBuilder;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.dialog.dialogBuilders.DialogBuilder;
import com.company.prodamgarage.models.loaders.PossibilitiesLoader;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import io.reactivex.Completable;

import java.util.List;
import java.util.Optional;

public class PossibilitiesEvent extends Event {

    private volatile boolean isFullyLoaded = false;

    private List<Possibility> apartmentPossibilities;
    private List<Possibility> businessPossibilities;
    private List<Possibility> educationPossibilities;

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

            apartmentPossibilities = PossibilitiesLoader.getPossibilitiesRepository(dialogFactory)
                    .blockingGet()
                    .getPossibilities(PossibilityType.APARTMENT);

            businessPossibilities = PossibilitiesLoader.getPossibilitiesRepository(dialogFactory)
                    .blockingGet()
                    .getPossibilities(PossibilityType.BUSINESS);

            educationPossibilities = PossibilitiesLoader.getPossibilitiesRepository(dialogFactory)
                    .blockingGet()
                    .getPossibilities(PossibilityType.EDUCATION);
            
            isFullyLoaded = true;
            completableEmitter.onComplete();
        });
    }

    public Optional<List<Possibility>> getApartmentPossibilities() {
        return Optional.ofNullable(apartmentPossibilities);
    }

    public Optional<List<Possibility>> getBusinessPossibilities() {
        return Optional.ofNullable(businessPossibilities);
    }

    public Optional<List<Possibility>> getEducationPossibilities() {
        return Optional.ofNullable(educationPossibilities);
    }
}
