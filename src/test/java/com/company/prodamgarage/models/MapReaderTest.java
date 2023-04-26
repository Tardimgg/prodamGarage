package com.company.prodamgarage.models;

import com.company.prodamgarage.models.eventModels.EventType;
import com.company.prodamgarage.models.loaders.MapReader;
import com.company.prodamgarage.models.mapModels.MapElement;
import com.company.prodamgarage.models.mapModels.MapRepository;
import com.company.prodamgarage.models.mapModels.SeasonType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapReaderTest {

    @Test
    void getMapRepositoryDefaultTest1() {
        MapRepository rep = MapReader.getMapRepository("/good_map_json.json").blockingGet();

        assertNotNull(rep);
        List<MapElement> mapElementList = rep.getMapList();
        assertNotNull(mapElementList);

        assertEquals(mapElementList.size(), 2);

        assertEquals(mapElementList.get(0).eventType, EventType.BAD);
        assertEquals(mapElementList.get(0).seasonType, SeasonType.SUMMER);

        assertEquals(mapElementList.get(1).eventType, EventType.GOOD);
        assertEquals(mapElementList.get(1).seasonType, SeasonType.AUTUMN);
    }

    @Test
    void getMapRepositoryDefaultTest2() {
        MapRepository rep = MapReader.getMapRepository("/bad_map_json.json").blockingGet();

        assertNotNull(rep);
        List<MapElement> mapElementList = rep.getMapList();
        assertNotNull(mapElementList);

        assertEquals(mapElementList.size(), 0);
    }
}
