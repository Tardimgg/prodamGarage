package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;
import java.util.ArrayList;

public class EventReader {
    private static EventsRepository eventsRepository;

    public EventsRepository getEventsRepository(){
        if(eventsRepository == null){
            //Gson gson = new Gson();
            JSONParser parser = new JSONParser();
            try(FileReader reader = new FileReader("src/main/resources/data/testJSON.json")){
                JSONObject jsonObj = (JSONObject) parser.parse(reader);
                //goodEventsRepository = gson.fromJson( reader, GoodEventsRepository.class);
                eventsRepository = new EventsRepository();
                eventsRepository.setGoodEventList((ArrayList<GoodEvent>) jsonObj.get("goodEventList"));
                eventsRepository.setBadEventList((ArrayList<BadEvent>) jsonObj.get("badEventList"));
                return eventsRepository;
            } catch (Exception e){
                System.out.println("Parsing Error " + e.toString());
            }
            return null;
        }else{
            return eventsRepository;
        }
    }
}
