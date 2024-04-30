package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.eventModels.NotificationEvent;

import java.util.List;

public class PlotRepository {
    private List<Event> plotEvents;


    public List<Event> getPlotEvents() {
        return plotEvents;
    }

    public void setPlotEvents(List<Event> plotEvents) {
        this.plotEvents = plotEvents;
    }
}
