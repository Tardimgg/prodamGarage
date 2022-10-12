package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.PlotRepository;
import com.company.prodamgarage.models.StdJsonParser;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.PlotEvent;
import com.company.prodamgarage.models.possibilityModels.PossibilitiesRepository;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.user.UserChanges;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Single;

import java.io.FileReader;
import java.util.*;

public class PlotLoader {
    private static String defaultPath = "src/main/resources/data/plotJSON.json";
    private static HashMap<String, PlotRepository> data = new HashMap<>();

    public static Single<PlotRepository> getPlotRepository(DialogFactory dialogFactory) {
        return getPlotRepository(dialogFactory, defaultPath);
    }
    public static Single<PlotRepository> getPlotRepository(DialogFactory dialogFactory,String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (FileReader reader = new FileReader(path)) {
                    PlotRepository plotRepository = new PlotRepository();

                    JsonObject jsonObj = (JsonObject) parser.parse(reader);

                    JsonArray plot_arr = (JsonArray) jsonObj.get("plotList");

                    List<Pair<Class<?>, Optional<List<Pair<Class<?>, ?>>>>> allTypesObj = Arrays.asList(
                            Pair.create(PlotEvent.class, Optional.of(List.of(Pair.create(DialogFactory.class, dialogFactory)))),
                            Pair.create(Pair.class, Optional.empty())
                    );

                    List<PlotEvent> targetPlotRepository = StdJsonParser.parseListJson(plot_arr, allTypesObj, PlotEvent.class);


                    plotRepository.setPlotEvents(targetPlotRepository);


                    data.put(path, plotRepository);
                    singleSubscriber.onSuccess(plotRepository);
                } catch (Exception e) {
                    singleSubscriber.onError(new Throwable("parsing error" + e));
                }
            } else {
                singleSubscriber.onSuccess(data.get(path));
            }
        });
    }
}

