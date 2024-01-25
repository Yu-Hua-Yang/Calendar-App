package ca.project.calendarapp.data

import ca.project.calendarapp.network.Weather
import ca.project.calendarapp.network.WeatherApiService
import retrofit2.http.QueryMap

interface WeatherRepository {
    suspend fun getWeather(@QueryMap queryParams: Map<String, String>): Weather
}

class NetworkWeatherRepository(private val weatherApiService: WeatherApiService) :WeatherRepository {
    override suspend fun getWeather(@QueryMap queryParams: Map<String, String>): Weather = weatherApiService.getWeather(queryParams)
}
