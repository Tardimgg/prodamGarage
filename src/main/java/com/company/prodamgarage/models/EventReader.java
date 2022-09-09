package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.GoodEventsRepository;
import com.google.gson.Gson;

import java.io.FileReader;

public class EventReader {
    public static GoodEventsRepository goodEventsRepository;

    public GoodEventsRepository getGoodEventsRepository(){
        if(goodEventsRepository == null){
            Gson gson = new Gson();
            try(FileReader reader = new FileReader("src/main/resources/data/testJSON.json")){
                goodEventsRepository = gson.fromJson( reader, GoodEventsRepository.class);
                return goodEventsRepository;
            } catch (Exception e){
                System.out.println("Parsing Error " + e.toString());
            }
            return null;
        }else{
            return goodEventsRepository;
        }
    }
}
