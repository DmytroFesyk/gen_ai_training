package com.epam.training.gen.ai.cleint.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;

public record CurrentWeatherResponse(
        Location location,
        @JsonProperty("current")
        @Description(" realtime weather information for a given city or location")
        WeatherParameters current
) {
}
