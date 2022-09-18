package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.BadEvent;
import com.company.prodamgarage.models.eventModels.EventsRepository;
import com.company.prodamgarage.models.eventModels.GoodEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapReaderTest {

    @Test
    void getMapRepositoryDefaultTest1() {
        MapReader reader = new MapReader();
        MapRepository rep = reader.getMapRepository("src/test/resources/good_map_json.json");

        assertNotNull(rep);
        List<Map> mapList = rep.getMapList();
        assertNotNull(mapList);

        assertEquals(mapList.size(), 2);

        assertEquals(mapList.get(0).eventType, EventType.BAD);
        assertEquals(mapList.get(0).seasonType, SeasonType.SUMMER);

        assertEquals(mapList.get(1).eventType, EventType.VERY_BAD);
        assertEquals(mapList.get(1).seasonType, SeasonType.AUTUMN);
    }

    @Test
    void getMapRepositoryDefaultTest2() {
        MapReader reader = new MapReader();
        MapRepository rep = reader.getMapRepository("src/test/resources/bad_map_json.json");

        assertNotNull(rep);
        List<Map> mapList = rep.getMapList();
        assertNotNull(mapList);
        
        assertEquals(mapList.size(), 0);
    }
}