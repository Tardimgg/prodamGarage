package com.company.prodamgarage.models.eventModels;

import java.util.List;

public class EventsRepository {
    private List<GoodEvent> goodEventList;
    private List<BadEvent> badEventList;

    public List<GoodEvent> getGoodEventList() {
        return goodEventList;
    }

    public void setGoodEventList(List<GoodEvent> goodEventList) {
        this.goodEventList = goodEventList;
    }

    public List<BadEvent> getBadEventList() {
        return badEventList;
    }

    public void setBadEventList(List<BadEvent> badEventList) {
        this.badEventList = badEventList;
    }

    public GoodEvent getRandomGoodEvent() {
        return (goodEventList.get((int) (Math.random() * goodEventList.size())));
    }

    public BadEvent getRandomBadEvent() {
        return (badEventList.get((int) (Math.random() * badEventList.size())));
    }
}
