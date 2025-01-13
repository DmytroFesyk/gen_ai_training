package com.epam.training.gen.ai.cleint.weather;

import com.epam.training.gen.ai.cleint.AvailableDialModelsResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


public interface WeatherClient {

    @GetExchange("v1/current.json")
    CurrentWeatherResponse gatCurrentWeather(@RequestParam("q") String location, @RequestParam("key") String apiKey);

    @GetExchange("v1/forecast.json")
    AvailableDialModelsResponse gatForecastWeather(@RequestParam("q") String location, @RequestParam("key") String apiKey);
}
