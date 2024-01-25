package ca.project.calendarapp.data

import ca.project.calendarapp.network.Weather
import ca.project.calendarapp.network.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface WeatherContainer {
    val weatherRepository: WeatherRepository
}

class DefaultWeatherContainer: WeatherContainer{
    private val baseUrl = "https://api.open-meteo.com/v1/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    override val weatherRepository: WeatherRepository by lazy {
        NetworkWeatherRepository(retrofitService)
    }


}