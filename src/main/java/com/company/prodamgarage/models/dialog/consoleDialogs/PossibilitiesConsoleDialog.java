package com.company.prodamgarage.models.dialog.consoleDialogs;

import com.company.prodamgarage.models.GameOver;
import com.company.prodamgarage.models.dialog.Dialog;
import com.company.prodamgarage.models.possibilityModels.Possibility;

import java.util.List;

public class PossibilitiesConsoleDialog implements Dialog {

    List<Possibility> apartmentPossibilities;
    List<Possibility> businessPossibilities;
    String title;

    public PossibilitiesConsoleDialog(String title, List<Possibility> apartmentPossibilities, List<Possibility> businessPossibilities) {
        this.title = title;
        this.apartmentPossibilities = apartmentPossibilities;
        this.businessPossibilities = businessPossibilities;
    }

    @Override
    public Object create() throws GameOver {
        return null;

    }
}
