package com.company.prodamgarage.models.dialog.dialogBuilders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import com.company.prodamgarage.models.user.UserChanges;

import java.util.ArrayList;
import java.util.List;

public class PossibilitiesDialogBuilder implements DialogBuilder {

    private DialogFactory factory;

    public PossibilitiesDialogBuilder(DialogFactory factory) {
        this.factory = factory;
    }

    List<Pair<PossibilityType, List<Possibility>>> possibilities = new ArrayList<>();

    String title;

    public PossibilitiesDialogBuilder setPossibilities(PossibilityType type, List<Possibility> possibilities) {
        this.possibilities.add(Pair.create(type, possibilities));
        return this;
    }

    @Override
    public DialogBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public Dialog build() {
        return factory.createPossibilitiesDialog(title, possibilities);
    }

}
