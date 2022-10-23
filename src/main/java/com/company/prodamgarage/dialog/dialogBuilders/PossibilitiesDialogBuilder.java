package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.List;

public class PossibilitiesDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public PossibilitiesDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    List<Possibility> apartmentPossibilities;
    List<Possibility> businessPossibilities;
    String title;

    public PossibilitiesDialogBuilder setApartmentPossibilities(List<Possibility> apartmentPossibilities) {
        this.apartmentPossibilities = apartmentPossibilities;
        return this;
    }

    public PossibilitiesDialogBuilder setBusinessPossibilities(List<Possibility> businessPossibilities) {
        this.businessPossibilities = businessPossibilities;
        return this;
    }

    @Override
    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Dialog build() {
        return factory.createPossibilitiesDialog(title, apartmentPossibilities, businessPossibilities);
    }

}
