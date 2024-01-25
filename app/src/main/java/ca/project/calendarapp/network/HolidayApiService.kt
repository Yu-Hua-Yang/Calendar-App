package ca.project.calendarapp.network


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.Locale.IsoCountryCode


private const val BASE_URL = "https://date.nager.at"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface HolidayApiService {
    @GET("/api/v3/PublicHolidays/{Year}/{CountryCode}")
    suspend fun getHoliday(
        @Path("Year") year: Int,
        @Path("CountryCode") countryCode: String
    ): List<Holiday>
}


object HolidayApi {
    val retrofitService : HolidayApiService by lazy {
        retrofit.create(HolidayApiService::class.java)
    }
}

