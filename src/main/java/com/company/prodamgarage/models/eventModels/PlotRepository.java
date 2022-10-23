package com.company.prodamgarage.models.eventModels;

import com.company.prodamgarage.models.eventModels.NotificationEvent;

import java.util.List;

public class PlotRepository {
    private List<NotificationEvent> plotEvents;


    public List<NotificationEvent> getPlotEvents() {
        return plotEvents;
    }

    public void setPlotEvents(List<NotificationEvent> plotEvents) {
        this.plotEvents = plotEvents;
    }
}
