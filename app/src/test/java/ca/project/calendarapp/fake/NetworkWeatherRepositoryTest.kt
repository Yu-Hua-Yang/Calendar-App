package ca.project.calendarapp.fake

import ca.project.calendarapp.data.NetworkWeatherRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkWeatherRepositoryTest {
    @Test
    fun networkWeatherRepository_getWeather() = runTest{
        val queries = mapOf(
            "latitude" to "54.9",
            "longitude" to "32.6",
            "current" to "temperature_2m",
            "daily" to "weather_code,temperature_2m_max,temperature_2m_min",
            "timezone" to "auto"
        )
        val repository = NetworkWeatherRepository(
            weatherApiService = FakeWeatherService()
        )
        assertEquals(FakeWeatherSource.weather, repository.getWeather(queries))
    }

}