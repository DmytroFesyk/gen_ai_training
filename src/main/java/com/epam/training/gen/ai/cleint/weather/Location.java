package com.epam.training.gen.ai.cleint.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;

public record Location(
        @JsonProperty("name")
        @Description("Location or city name")
        String name,
        @JsonProperty("region")
        @Description("Region or state name")
        String region,
        @JsonProperty("country")
        @Description("Country name")
        String country,
        @JsonProperty("lat")
        @Description("Location latitude in decimal degree")
        double latitude,
        @JsonProperty("lon")
        @Description("Location longitude in decimal degree")
        double longitude,
        @JsonProperty("tz_id")
        @Description("Time zone ID")
        String timeZoneId,
        @JsonProperty("localtime_epoch")
        @Description("Local date and time in unix time")
        long localtimeEpoch,
        @JsonProperty("localtime")
        @Description("Local date and time")
        String localtime
) {
}
