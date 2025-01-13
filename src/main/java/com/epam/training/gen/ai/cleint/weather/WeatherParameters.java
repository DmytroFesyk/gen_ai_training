package com.epam.training.gen.ai.cleint.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;

public record WeatherParameters(
        @JsonProperty("temp_c")
        @Description("Temperature in celsius")
        double temperatureCelsius,
        @JsonProperty("condition")
        @Description("Weather condition")
        WeatherCondition condition,
        @JsonProperty("wind_kph")
        @Description("Wind speed in kilometer per hour")
        double windSpeedKilometerPerHour,
        @JsonProperty("wind_dir")
        @Description("Wind direction as 16 point compass. e.g.: NSW")
        String windDirection,
        @JsonProperty("humidity")
        @Description("Humidity in percentage")
        int humidity,
        @JsonProperty("cloud")
        @Description("Cloud coverage in percentage")
        int cloudCoverage,
        @JsonProperty("feelslike_c")
        @Description("Feels like temperature in celsius")
        double feelsLikeCelsius

) {

    public record WeatherCondition(
            @JsonProperty("text")
            @Description("Weather condition text")
            String text
    ) {
    }
}
