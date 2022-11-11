package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.Pair;

public class Map {

    Pair<Integer, Integer>[] positions = new Pair[]{
            Pair.create(0, -380),
            Pair.create(-122, -380),
            Pair.create(-244, -380),
            Pair.create(-366, -380),
            Pair.create(-488, -380),
            Pair.create(-610, -380),
            Pair.create(-610, -285)
    };

    private int currentPos = 0;

    Pair<Integer, Integer> getNextPos(double height, double width) {
        Pair<Integer, Integer> res = positions[currentPos].clone();

        res.setKey((int) (res.getKey() * width / 1550));
        res.setValue((int) (res.getValue() * height / 880));

        ++currentPos;
        currentPos %= positions.length;

        return res;
    }
}
