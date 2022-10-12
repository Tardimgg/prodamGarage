package com.company.prodamgarage.models.loaders;

import com.company.prodamgarage.Pair;
import com.company.prodamgarage.models.StdJsonParser;
import com.company.prodamgarage.models.possibilityModels.PossibilitiesRepository;
import com.company.prodamgarage.models.possibilityModels.Possibility;
import com.company.prodamgarage.models.user.UserChanges;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.reactivex.Single;

import java.io.FileReader;
import java.util.*;

public class PossibilitiesLoader {
    private static String defaultPath = "src/main/resources/data/possibilitiesJSON.json";
    private static HashMap<String, PossibilitiesRepository> data = new HashMap<>();

    public static Single<PossibilitiesRepository> getPossibilitiesRepository() {
        return getPossibilitiesRepository(defaultPath);
    }

    public static Single<PossibilitiesRepository> getPossibilitiesRepository(String path) {
        return Single.create((singleSubscriber) -> {
            if (!data.containsKey(path)) {
                JsonParser parser = new JsonParser();
                try (FileReader reader = new FileReader(path)) {
                    PossibilitiesRepository possibilitiesRepository = new PossibilitiesRepository();
                    JsonObject jsonObj = (JsonObject) parser.parse(reader);

                    JsonArray apPossibility_arr = (JsonArray) jsonObj.get("apartmentPossibilitiesList");
                    JsonArray bisPossibility_arr = (JsonArray) jsonObj.get("businessPossibilitiesList");
                    JsonArray edPossibility_arr = (JsonArray) jsonObj.get("businessPossibilitiesList");

                    List<Pair<Class<?>, Optional<List<Pair<Class<?>, ?>>>>> allTypesObj = Arrays.asList(
                            Pair.create(Possibility.class, Optional.empty()),
                            Pair.create(UserChanges.class, Optional.empty()),
                            Pair.create(Pair.class, Optional.empty())
                    );

                    List<Possibility> targetApRepository = StdJsonParser.parseListJson(apPossibility_arr, allTypesObj, Possibility.class);
                    List<Possibility> targetBisRepository = StdJsonParser.parseListJson(bisPossibility_arr, allTypesObj, Possibility.class);
                    List<Possibility> targetEdRepository = StdJsonParser.parseListJson(edPossibility_arr, allTypesObj, Possibility.class);

                    possibilitiesRepository.setApartmentPossibilities(targetApRepository);
                    possibilitiesRepository.setBusinessPossibilities(targetBisRepository);
                    possibilitiesRepository.setEducationPossibilities(targetEdRepository);

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

