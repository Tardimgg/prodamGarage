package com.company.prodamgarage.models;

import java.util.List;

public class MapRepository {
    private List<Map> mapList;


    public List<Map> getMapList(){
        return mapList;
    }
    public void setMapList(List<Map> list){
        this.mapList = list;
    }
}
