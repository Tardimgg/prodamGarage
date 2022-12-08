package com.company.prodamgarage;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WeatherModel {

    private static Optional<Pattern> temperatureRegex = Optional.empty();

    static {
        try {
            temperatureRegex = Optional.of(Pattern.compile("id=\"col1\">(.*?)<span>.*?</td>"));
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static Flowable<String> getWeather() {
        return Flowable.create(flowableEmitter -> {


            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = Executors.defaultThreadFactory().newThread(r);
                t.setDaemon(true);
                return t;
            });
            executor.scheduleAtFixedRate(() -> {

                try {
                    URL url = new URL("https://realmeteo.ru/moscow/3/current");

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null)
                        stringBuilder.append(inputLine);

                    reader.close();

                    String fullResponse = stringBuilder.toString();
                    fullResponse = fullResponse.replace("\n", "");

                    String finalFullResponse = fullResponse;
                    temperatureRegex.ifPresent(pattern -> {
                        Matcher matcher = pattern.matcher(finalFullResponse);

                        if (matcher.find()) {
                            flowableEmitter.onNext(matcher.group(1).trim());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }, 0, 10, TimeUnit.SECONDS);

        }, BackpressureStrategy.DROP);


    }
}
