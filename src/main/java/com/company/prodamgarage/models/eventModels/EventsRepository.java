package com.company.prodamgarage.models.eventModels;

import java.util.ArrayList;

public class EventsRepository {
    private ArrayList<GoodEvent> goodEventList;
    private ArrayList<BadEvent> badEventList;

    public ArrayList<GoodEvent> getGoodEventList() {
        return goodEventList;
    }
    public void setGoodEventList(ArrayList<GoodEvent> goodEventList) {
        this.goodEventList = goodEventList;
    }
    public ArrayList<BadEvent> getBadEventList() {
        return badEventList;
    }
    public void setBadEventList(ArrayList<BadEvent> badEventList) {
        this.badEventList = badEventList;
    }
    public Object getRandomGoodEvent(){
        return (goodEventList.get( (int) ( Math.random() * goodEventList.size() )));
    }
    public Object getRandomBadEvent(){
        return (badEventList.get( (int) ( Math.random() * badEventList.size() )));
    }
}
