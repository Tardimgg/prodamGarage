package com.company.prodamgarage.models.possibilityModels;

import java.util.HashMap;
import java.util.List;

public class PossibilitiesRepository {
    private HashMap<String, List<Possibility>> possibilities = new HashMap<>();

    public List<Possibility> getApartmentPossibilities() {
        return possibilities.get("ApartmentPossibilities");
    }

    public void setApartmentPossibilities(List<Possibility> apartmentPossibilities) {
        this.possibilities.put("ApartmentPossibilities", apartmentPossibilities);
    }

    public List<Possibility> getBusinessPossibilities() {
        return possibilities.get("BusinessPossibilities");
    }

    public void setBusinessPossibilities(List<Possibility> businessPossibilities) {
        this.possibilities.put("BusinessPossibilities", businessPossibilities);
    }
    public List<Possibility> getEducationPossibilities() {
        return possibilities.get("EducationPossibilities");
    }

    public void setEducationPossibilities(List<Possibility> educationPossibilities) {
        this.possibilities.put("EducationPossibilities", educationPossibilities);
    }
}
