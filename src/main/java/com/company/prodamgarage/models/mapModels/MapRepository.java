package com.company.prodamgarage.models.mapModels;

import com.company.prodamgarage.models.mapModels.MapElement;

import java.util.List;

public class MapRepository {
    private List<MapElement> mapElementList;


    public List<MapElement> getMapList(){
        return mapElementList;
    }
    public void setMapList(List<MapElement> list){
        this.mapElementList = list;
    }
}
