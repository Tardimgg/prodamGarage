package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.PlotEvent;
import com.company.prodamgarage.models.possibilityModels.Possibility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlotRepository {
    private List<PlotEvent> plotEvents;


    public List<PlotEvent> getPlotEvents() {
        return plotEvents;
    }

    public void setPlotEvents(List<PlotEvent> plotEvents) {
        this.plotEvents = plotEvents;
    }
}
