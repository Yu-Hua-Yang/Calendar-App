import ca.project.calendarapp.network.Holiday
import ca.project.calendarapp.network.HolidayApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class HolidayApiTest {

    @Test
    fun ApiTest() = runBlocking {

        val holidayApi = mockk<HolidayApi>()

        val expectedHoliday = Holiday(date = "2023-01-01", name = "New Year's Day")
        val expectedResponse = listOf(expectedHoliday)

        // Mock the API call to return the expected response
        coEvery {

            holidayApi.retrofitService.getHoliday(2023, "New Year's Day")


        } returns Response.success(expectedResponse)

        // Perform the API call
        val actualResponse = holidayApi.getHoliday(2023, "US")

        // Verify the results
        assertEquals(expectedResponse, actualResponse.body())
    }

//    @Test
//    fun `test error in holiday API call`() = runBlocking {
//        // Create a mock for the Retrofit service
//        val holidayApi = mockk<HolidayApi>()
//
//        // Mock the API call to simulate an error response
//        coEvery { holidayApi.getHoliday(any(), any()) } returns Response.error(404, "Not Found".toResponseBody())
//
//        // Perform the API call
//        val actualResponse = holidayApi.getHoliday(2023, "US")
//
//        // Verify that the response is an error
//        assertEquals(null, actualResponse.body())
//        assertEquals(404, actualResponse.code())
//    }
}
