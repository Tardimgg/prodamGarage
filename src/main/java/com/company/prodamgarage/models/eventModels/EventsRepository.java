package com.company.prodamgarage.models.eventModels;

import java.util.List;

public class EventsRepository {
    private List<Event> goodEventList;
    private List<Event> badEventList;

    public List<Event> getGoodEventList() {
        return goodEventList;
    }

    public void setGoodEventList(List<Event> goodEventList) { this.goodEventList = goodEventList; }

    public List<Event> getBadEventList() {
        return badEventList;
    }

    public void setBadEventList(List<Event> badEventList) { this.badEventList = badEventList; }

    public Event getRandomGoodEvent() {
        return (goodEventList.get((int) (Math.random() * goodEventList.size())));
    }

    public Event getRandomBadEvent() { return (badEventList.get((int) (Math.random() * badEventList.size()))); }
}
