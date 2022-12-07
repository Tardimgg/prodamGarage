package com.company.prodamgarage.controllers.game;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.user.User;
import io.reactivex.Single;

public class Map {

    private Map(int currentPos) {
        this.currentPos = currentPos;
    }

    public static Single<Map> newInstance() {
        return Single.create(singleEmitter -> {

            User user = User.getInstance().blockingGet();
            singleEmitter.onSuccess(new Map(user.getCurrentTime() % positions.length));
        });
    }

    private static final Pair<Integer, Integer>[] positions = new Pair[]{
            Pair.create(0, -380),
            Pair.create(-122, -380),
            Pair.create(-244, -380),
            Pair.create(-366, -380),
            Pair.create(-488, -380),
            Pair.create(-610, -380),
            Pair.create(-610, -285),
            Pair.create(-610, -220),
            Pair.create(-610, -130),
            Pair.create(-380, 380),
            Pair.create(-258, 380),
            Pair.create(-136, 380),
            Pair.create(-14, 380),
            Pair.create(108, 380),
            Pair.create(230, 380),
            Pair.create(352, 380),
            Pair.create(610, -20),
            Pair.create(610, -90),
            Pair.create(610, -150),
            Pair.create(610, -224),
            Pair.create(610, -300),
            Pair.create(610, -380),
            Pair.create(488, -380),
            Pair.create(366, -380),
            Pair.create(244, -380),
            Pair.create(122, -380),
    };

    private int currentPos;

    Pair<Integer, Integer> getNextPos(double height, double width) {
        Pair<Integer, Integer> res = positions[currentPos].clone();

        res.setKey((int) (res.getKey() * width / 1550));
        res.setValue((int) (res.getValue() * height / 880));

        ++currentPos;
        currentPos %= positions.length;

        return res;
    }
}
