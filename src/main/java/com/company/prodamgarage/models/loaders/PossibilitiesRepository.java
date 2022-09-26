package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.models.eventModels.Event;

import java.util.List;

public class PossibilitiesRepository {
    private List<BusinessPossibility> businessPossibilities;
    private List<EducationPossibility> educationPossibilities;

    public void setEducationPossibilities(List<EducationPossibility> educationPossibilities) {
        this.educationPossibilities = educationPossibilities;
    }

    public void setBusinessPossibilities(List<BusinessPossibility> businessPossibilities) {
        this.businessPossibilities = businessPossibilities;
    }

    public List<BusinessPossibility> getBusinessPossibilities() {
        return businessPossibilities;
    }

    public List<EducationPossibility> getEducationPossibilities() {
        return educationPossibilities;
    }

    public BusinessPossibility getRandomBusinessPossibility() {
        return (businessPossibilities.get((int) (Math.random() * businessPossibilities.size())));
    }

    public EducationPossibility getRandomEducationPossibility() {
        return (educationPossibilities.get((int) (Math.random() * educationPossibilities.size())));
    }
}
