package ca.project.calendarapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentUnits(
    val time: String,
    val interval: String,
    @SerialName(value = "temperature_2m")
    val temperature2m: String,
    @SerialName(value = "precipitation")
    val precipitation: String
)
@Serializable
data class Current(
    val time: String,
    val interval: Double,
    @SerialName(value = "temperature_2m")
    val temperature2m: Double,
    @SerialName(value = "precipitation")
    val precipitation: Double
)
@Serializable
data class DailyUnits(
    val time: String,
    @SerialName(value = "weather_code")
    val weatherCode: String,
    @SerialName(value = "temperature_2m_max")
    val temperature2mMax: String,
    @SerialName(value = "temperature_2m_min")
    val temperature2mMin: String,
    @SerialName(value = "precipitation_probability_max")
    val precipitation_probability_max: String
)
@Serializable
data class Daily(
    val time: List<String>,
    @SerialName(value = "weather_code")
    val weatherCode: List<Int>,
    @SerialName(value = "temperature_2m_max")
    val temperature2mMax: List<Double>,
    @SerialName(value = "temperature_2m_min")
    val temperature2mMin: List<Double>,
    @SerialName(value = "precipitation_probability_max")
    val precipitation_probability_max: List<Int>
)
@Serializable
data class Weather(
    val latitude: Double,
    val longitude: Double,
    @SerialName(value = "generationtime_ms")
    val generationTimeMs: Double,
    @SerialName(value = "utc_offset_seconds")
    val utcOffsetSeconds: Double,
    val timezone: String,
    @SerialName(value = "timezone_abbreviation")
    val timezoneAbbreviation: String,
    val elevation: Double,
    @SerialName(value = "current_units")
    val currentUnits: CurrentUnits,
    val current: Current,
    @SerialName(value = "daily_units")
    val dailyUnits: DailyUnits,
    val daily: Daily,
)


