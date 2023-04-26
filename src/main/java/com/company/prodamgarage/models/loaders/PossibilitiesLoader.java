package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.StdJsonParser;
import com.company.prodamgarage.models.conditions.Conditions;
import com.company.prodamgarage.models.conditions.ConditionsTypes;
import com.company.prodamgarage.models.dialog.factory.DialogFactory;
import com.company.prodamgarage.models.eventModels.NotificationEvent;
import com.company.prodamgarage.models.eventModels.SelectionEvent;
import com.company.prodamgarage.models.possibilityModels.PossibilitiesRepository;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.possibilityModels.PossibilityType;
import com.company.prodamgarage.models.user.PropertyType;
import com.company.prodamgarage.models.user.UserChanges;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Single;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class PossibilitiesLoader {
    private static String defaultPath = "/data/possibilitiesJSON.json";
    private static HashMap<String, PossibilitiesRepository> data = new HashMap<>();

    public static Single<PossibilitiesRepository> getPossibilitiesRepository(DialogFactory dialogFactory) {
        return getPossibilitiesRepository(dialogFactory, defaultPath);
    }

    public static Single<PossibilitiesRepository> getPossibilitiesRepository(DialogFactory dialogFactory, String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (InputStream reader = PossibilitiesLoader.class.getResourceAsStream(path)) {
                    PossibilitiesRepository possibilitiesRepository = new PossibilitiesRepository();
                    JsonObject jsonObj = (JsonObject) parser.parse(new InputStreamReader(reader));

                    JsonArray apPossibility_arr = (JsonArray) jsonObj.get("apartmentPossibilitiesList");
                    JsonArray bisPossibility_arr = (JsonArray) jsonObj.get("businessPossibilitiesList");
                    JsonArray edPossibility_arr = (JsonArray) jsonObj.get("educationPossibilitiesList");

                    List<Pair<Class<?>, Optional<List<Pair<Class<?>, ?>>>>> allTypesObj = Arrays.asList(
                            Pair.create(Possibility.class, Optional.empty()),
                            Pair.create(UserChanges.class, Optional.empty()),
                            Pair.create(Pair.class, Optional.empty()),
                            Pair.create(PropertyType.class, Optional.empty()),
//                            Pair.create(String.class, Optional.empty()),
//                            Pair.create(Integer.class, Optional.empty()),
                            Pair.create(NotificationEvent.class, Optional.of(List.of(Pair.create(DialogFactory.class, dialogFactory)))),
                            Pair.create(SelectionEvent.class, Optional.of(List.of(Pair.create(DialogFactory.class, dialogFactory)))),
                            Pair.create(Conditions.class, Optional.empty()),
                            Pair.create(ConditionsTypes.class, Optional.empty())
                    );

                    List<Possibility> targetApRepository = StdJsonParser.parseListJson(apPossibility_arr, allTypesObj, Possibility.class);
                    List<Possibility> targetBisRepository = StdJsonParser.parseListJson(bisPossibility_arr, allTypesObj, Possibility.class);
                    List<Possibility> targetEdRepository = StdJsonParser.parseListJson(edPossibility_arr, allTypesObj, Possibility.class);

                    possibilitiesRepository.setPossibilities(PossibilityType.APARTMENT, targetApRepository);
                    possibilitiesRepository.setPossibilities(PossibilityType.BUSINESS, targetBisRepository);
                    possibilitiesRepository.setPossibilities(PossibilityType.EDUCATION, targetEdRepository);

                    data.put(path, possibilitiesRepository);
                    singleSubscriber.onSuccess(possibilitiesRepository);
                } catch (Exception e) {
                    singleSubscriber.onError(new Throwable("parsing error" + e));
                }
            } else {
                singleSubscriber.onSuccess(data.get(path));
            }
        });
    }
}

