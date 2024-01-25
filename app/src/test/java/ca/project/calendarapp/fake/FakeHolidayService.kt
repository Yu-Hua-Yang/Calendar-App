package ca.project.calendarapp.fake

import ca.project.calendarapp.network.Holiday
import ca.project.calendarapp.network.HolidayApiService
import retrofit2.http.QueryMap

class FakeHolidayService : HolidayApiService {
    override suspend fun getHoliday(queryParams: Map<String, String>): List<Holiday> {
        // Assuming FakeWeatherSource.weather is a list of holidays
        return listOf(FakeWeatherSource.weather)
    }
    // Manually merge it if it keeps not working
}