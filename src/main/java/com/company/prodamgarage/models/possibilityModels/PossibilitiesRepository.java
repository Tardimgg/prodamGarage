package com.company.prodamgarage.models.possibilityModels;

import com.company.prodamgarage.models.possibilityModels.Possibility;

import java.util.List;

public class PossibilitiesRepository {
    private List<Possibility> possibilities;



    public List<Possibility> getPossibilities() {
        return possibilities;
    }

    public void setPossibilities(List<Possibility> possibilities) {
        this.possibilities = possibilities;
    }
}
