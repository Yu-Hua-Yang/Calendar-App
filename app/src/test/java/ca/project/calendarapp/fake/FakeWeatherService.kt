package ca.project.calendarapp.fake

import ca.project.calendarapp.network.Weather
import ca.project.calendarapp.network.WeatherApiService
import retrofit2.http.QueryMap

class FakeWeatherService: WeatherApiService {
    override suspend fun getWeather(@QueryMap queryParams: Map<String, String>): Weather {
        return FakeWeatherSource.weather
    }

}