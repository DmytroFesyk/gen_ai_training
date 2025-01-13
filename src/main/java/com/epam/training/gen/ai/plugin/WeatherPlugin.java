package com.epam.training.gen.ai.plugin;

import com.epam.training.gen.ai.cleint.weather.CurrentWeatherResponse;
import com.epam.training.gen.ai.cleint.weather.WeatherClient;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Optional;

@RequiredArgsConstructor
public class WeatherPlugin {

    private final WeatherClient weatherClient;
    private final String weatherApiKey;

    @DefineKernelFunction(name = "getWeatherByCoordinates", description = "Provides current/realtime weather for given coordinates (latitude, longitude). Do not provide weather forecast information for future dates.")
    public CurrentWeatherResponse getWeatherByCoordinates(
            @KernelFunctionParameter(description = "Latitude of place to provide weather for", name = "latitude")
            double latitude,
            @KernelFunctionParameter(description = "Longitude of place to provide weather for", name = "longitude")
            double longitude) {
        return weatherClient.gatCurrentWeather(latitude + "," + longitude, weatherApiKey);
    }

    @DefineKernelFunction(name = "getWeatherByLocationName",
            description = "Provides current/realtime weather for location name ( eg. 'city' or 'country city' name). Do not provide weather forecast information for future dates.")
    public CurrentWeatherResponse getWeatherByLocationName(
            @KernelFunctionParameter(description = "City name for which to provide weather", name = "cityName")
            String cityName,
            @KernelFunctionParameter(
                    description = "the country in which the city for which the weather forecast must be provided is located",
                    name = "countryName",
                    required = false)
            String countryName) {
        {
            val location = Optional.ofNullable(countryName)
                    .map(country -> country + " " + cityName)
                    .orElse(cityName);
            return weatherClient.gatCurrentWeather(location, weatherApiKey);
        }
    }
}
