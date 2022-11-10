package com.company.prodamgarage.models.possibilityModels;

import java.util.HashMap;
import java.util.List;

public class PossibilitiesRepository {
    private HashMap<PossibilityType, List<Possibility>> possibilities = new HashMap<>();

    public void setPossibilities(PossibilityType type, List<Possibility> possibilities) {
        this.possibilities.put(type, possibilities);
    }

    public List<Possibility> getPossibilities(PossibilityType type) {
        return this.possibilities.get(type);
    }
}
