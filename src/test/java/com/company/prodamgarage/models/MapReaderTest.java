package com.company.prodamgarage.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapReaderTest {

    @Test
    void getMapRepositoryDefaultTest1() {
        MapRepository rep = MapReader.getMapRepository("src/test/resources/good_map_json.json").blockingGet();

        assertNotNull(rep);
        List<MapElement> mapElementList = rep.getMapList();
        assertNotNull(mapElementList);

        assertEquals(mapElementList.size(), 2);

        assertEquals(mapElementList.get(0).eventType, EventType.BAD);
        assertEquals(mapElementList.get(0).seasonType, SeasonType.SUMMER);

        assertEquals(mapElementList.get(1).eventType, EventType.VERY_BAD);
        assertEquals(mapElementList.get(1).seasonType, SeasonType.AUTUMN);
    }

    @Test
    void getMapRepositoryDefaultTest2() {
        MapRepository rep = MapReader.getMapRepository("src/test/resources/bad_map_json.json").blockingGet();

        assertNotNull(rep);
        List<MapElement> mapElementList = rep.getMapList();
        assertNotNull(mapElementList);

        assertEquals(mapElementList.size(), 0);
    }
}