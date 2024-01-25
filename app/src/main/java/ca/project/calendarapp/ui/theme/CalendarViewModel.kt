package ca.project.calendarapp.ui.theme

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.EventsRepository
import ca.project.calendarapp.network.Weather
import ca.project.calendarapp.network.WeatherApi
import ca.project.calendarapp.data.Game
import ca.project.calendarapp.data.NetworkWeatherRepository
import ca.project.calendarapp.network.Holiday
import ca.project.calendarapp.network.HolidayApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface WeatherUiState {
    data class Success(val weather: String) : WeatherUiState
    object Error : WeatherUiState
    object Loading : WeatherUiState
}

sealed interface HolidayUiState {
    data class Success(val holiday: String) : HolidayUiState
    object Error : HolidayUiState
    object Loading : HolidayUiState
}

class CalendarViewModel(private val eventsRepository: EventsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> get() = _uiState.asStateFlow()
    private val coroutineScope = CoroutineScope((Dispatchers.Main))
    var weatherUiState: WeatherUiState by mutableStateOf(WeatherUiState.Loading)
    var holidayUiState: HolidayUiState by mutableStateOf(HolidayUiState.Loading)
    var holidays: List<Holiday>? by mutableStateOf(null)


//    init {
//        getHolidayFromApi()
//    }
    fun getWeatherFromApi(lat: Double, long: Double) {
        val queries = mapOf(
            "latitude" to (lat).toString(),
            "longitude" to (long).toString(),
            "current" to "temperature_2m,precipitation,",
            "daily" to "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max",
            "timezone" to "auto"
        )
        //check if weather was already fetched
        if (_uiState.value.weather.longitude == 0.0) {
            viewModelScope.launch {
                weatherUiState = WeatherUiState.Loading
                weatherUiState =
                    try {
                        _uiState.value.weather = WeatherApi.retrofitService.getWeather(queries)
//                        val marsPhotosRepository = NetworkWeatherRepository()
//                        _uiState.value.weather = marsPhotosRepository.getWeather(queries)

                        WeatherUiState.Success(
                            "Success: Weather was fetched"
                        )
                    } catch (e: IOException) {
                        WeatherUiState.Error
                    } catch (e: HttpException) {
                        WeatherUiState.Error
                    }
            }
        }
    }

    fun getHolidayFromApi() {
        viewModelScope.launch {
            holidayUiState = HolidayUiState.Loading
            holidayUiState = try {
                val listResult = HolidayApi.retrofitService.getHoliday(_uiState.value.holidYear,
                    uiState.value.holidCountryCode)
                holidays = listResult
                HolidayUiState.Success(
                    "Success:"
                )

            } catch (e: IOException) {
                HolidayUiState.Error
            } catch (e: HttpException) {
                HolidayUiState.Error
            }
        }
    }

    fun setHolidayYear(year: Int){
        _uiState.value.holidYear = year
    }

    fun setHolidayCountryCode(countryCode: String){
        _uiState.value.holidCountryCode = countryCode
    }

    fun transformDateForApi(date: String): String{
        val strings = date.split("/")
        return strings[2] + "-" + strings[0] + "-" + strings[1]
    }

    fun checkIfDateIsWithinNextSevenDays(date: String): Int{
        var validDateIndex: Int = -1
        val validDates = _uiState.value.weather.daily.time
        for ((index, validDate) in validDates.withIndex()) {
            if (date == validDate){
                validDateIndex = index
            }
        }
        return validDateIndex
    }

    fun getCurrentTemperature():String{
        return _uiState.value.weather.current.temperature2m.toString()
    }

    fun getCurrentPrecipitationAmount(): String {
        return _uiState.value.weather.current.precipitation.toString()
    }

    fun getWeatherIcon(index: Int): String{
        val code = _uiState.value.weather.daily.weatherCode[index]
        val result = when {
            code == 0 -> "sun"
            code in 1..3 -> "cloud"
            code in 50..65 -> "rain"
            code in 66..86 -> "snow"
            code > 94 -> "storm"
            else -> "sun"
        }
        return result
    }

    fun getPrecipitationPercentage(date: String): String{
        var correctIndex: Int = 0
        val validDates = _uiState.value.weather.daily.time
        for ((index, validDate) in validDates.withIndex()) {
            if (date == validDate){
                correctIndex = index
            }
        }
        return _uiState.value.weather.daily.precipitation_probability_max[correctIndex].toString()
    }

    fun getMaxTemp(date: String): String{
        var correctIndex: Int = 0
        val validDates = _uiState.value.weather.daily.time
        for ((index, validDate) in validDates.withIndex()) {
            if (date == validDate){
                correctIndex = index
            }
        }
        return _uiState.value.weather.daily.temperature2mMax[correctIndex].toString()
    }

    fun getMinTemp(date: String): String{
        var correctIndex: Int = 0
        val validDates = _uiState.value.weather.daily.time
        for ((index, validDate) in validDates.withIndex()) {
            if (date == validDate){
                correctIndex = index
            }
        }
        return _uiState.value.weather.daily.temperature2mMin[correctIndex].toString()
    }

    fun getCurrent(): Event{
        return _uiState.value.currentEvent
    }

    fun getCurrentGame(): Game {
        return _uiState.value.currentGame
    }

    fun setCurrent(event: Event){
        _uiState.value.currentEvent = event
        getGame(event.gameId)
    }

    fun getDate(): String {
        return _uiState.value.date
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addDay(){
        val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        val date = LocalDate.parse(_uiState.value.date, formatter)
        val newDate = date.plusDays(1)
        val newDateString: String = newDate.format(formatter)
        _uiState.value.date = newDateString
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun minusDay(){
        val formatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        val date = LocalDate.parse(_uiState.value.date, formatter)
        val newDate = date.minusDays(1)
        val newDateString: String = newDate.format(formatter)
        _uiState.value.date = newDateString
    }
    fun addEvent(event: Event, game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            val job = async { addGame(game) }
            job.await()
            var newGame = _uiState.value.currentGame
            event.gameId = newGame.id
            eventsRepository.insertEvent(event)
            getEventOnCreation(event)
        }
    }

    fun testAddEvent(event: Event, game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            eventsRepository.insertGame(game)
            _uiState.value.currentGame = game
//            testGetGameOnCreation(game)
            event.gameId = game.id
            eventsRepository.insertEvent(event)
            _uiState.value.currentEvent = event
//            getEventOnCreation(event)
        }
    }


    suspend fun addGame(game: Game) {
        viewModelScope.async(Dispatchers.IO) {
            eventsRepository.insertGame(game)
            getGameOnCreation(game)
        }.await()
    }

    fun testAddGame(game: Game) {
        viewModelScope.launch{
            eventsRepository.insertGame(game)
            testGetGameOnCreation(game)
        }
    }

    fun updateEvent(updatedEvent: Event, updatedGame: Game) {
        coroutineScope.launch(Dispatchers.IO) {
            val job = async{ updateGame(updatedGame) }
            job.await()
            eventsRepository.updateEvent(updatedEvent)
            _uiState.value.currentEvent = updatedEvent
        }
    }

    private fun updateGame(updatedGame: Game) {
        coroutineScope.async(Dispatchers.IO) {
            eventsRepository.updateGame(updatedGame)
            _uiState.value.currentGame = updatedGame
        }
    }

    fun deleteEvent(event: Event, game: Game) {
        coroutineScope.launch(Dispatchers.IO) {
            eventsRepository.deleteGame(game)
            eventsRepository.deleteEvent(event)
        }
    }

    //Get Event that matches the event for on creation
    private fun getEventOnCreation(event: Event) {
        coroutineScope.launch {
            try {
                val newEvent = eventsRepository.getEventThatMatchStream(
                    event.eventDate,
                    event.startTime,
                    event.endTime,
                    event.title,
                    event.location,
                    event.description
                ).first()
                println(event.title+ event.location+ event.description)
                println(newEvent.title)
                _uiState.value.currentEvent = newEvent
            } catch (e: Exception) {
                Log.e("getEventOnCreation", "An error occurred")
            }
        }
    }

    fun getEventByEvent(event: Event): Event? {
        var result: Event? = null
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val newEvent = eventsRepository.getEventThatMatchStream(
                    event.eventDate,
                    event.startTime,
                    event.endTime,
                    event.title,
                    event.location,
                    event.description
                ).first()
                result = newEvent
            } catch (e: Exception) {
                Log.e("getEventOnCreation", "An error occurred")
            }
        }
        return result
    }

    private suspend fun getGameOnCreation(game: Game) {
        coroutineScope.async(Dispatchers.IO) {
            try {
                val newGame = eventsRepository.getGameThatMatchStream(
                    game.tournamentName,
                    game.game,
                    game.genre,
                    game.players,
                    game.prizePool
                ).first()
                _uiState.value = _uiState.value.copy(currentGame = newGame)
            } catch (e: Exception) {
                Log.e("getGameOnCreation", "An error occurred")
            }
        }.await()
    }

    fun testGetGameOnCreation(game: Game) {
        runBlocking {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    val newGame = eventsRepository.getGameThatMatchStream(
                        game.tournamentName,
                        game.game,
                        game.genre,
                        game.players,
                        game.prizePool
                    ).first()
                    _uiState.value.currentGame = newGame
                } catch (e: Exception) {
                    Log.e("getGameOnCreation", "An error occurred")
                }
            }
        }
    }

    fun getGameByGame(game: Game): Game? {
        var result: Game? = null
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val newGame = eventsRepository.getGameThatMatchStream(
                    game.tournamentName,
                    game.game,
                    game.genre,
                    game.players,
                    game.prizePool
                ).first()
                result = newGame

            } catch (e: Exception) {
                Log.e("getGameOnCreation", "An error occurred")
            }
        }
        return result
    }

    private fun getGame(gameId: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            try{
                val game = eventsRepository.getGameByGameIdStream(gameId).first()
                _uiState.value = _uiState.value.copy(currentGame = game)
            } catch (e: Exception) {
                Log.e("getGame", "An error occurred")
            }
        }
    }

    //Get Event For Day
    fun getEventByDay(date: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try{
                eventsRepository.getEventsByDayStream(date).collect { dbEvent ->
                    val mutableList: MutableList<Event> = dbEvent.toMutableList()
                    _uiState.value.dayEvents = mutableList
                }
            } catch (e: Exception) {
                Log.e("getEventByDay", "An error occurred")
            }
        }
    }

    fun getDayEvents(): MutableList<Event>{
        return _uiState.value.dayEvents
    }

    //Get Events For Month
    fun getEventsByMonth(date: String) {
        coroutineScope.launch(Dispatchers.IO) {
            try{
                val dateSplit = date.split('/')
                val month = dateSplit[0]
                val year = dateSplit[2]
                eventsRepository.getEventsByMonthStream(month, year).collect { dbEvent ->
                    val mutableList: MutableList<Event> = dbEvent.toMutableList()
                    _uiState.value = _uiState.value.copy(monthEvents = mutableList)
                }
            } catch (e: Exception) {
                Log.e("getEventsByMonth", "An error occurred")
            }
        }
    }

    fun setDay (newDate:String) {
        _uiState.value = _uiState.value.copy(date = newDate)
    }


    fun getMonthEvents(): MutableList<Event> {
        return uiState.value.monthEvents
    }
//    suspend fun getAllEvents(): MutableList<Event> {
//        return withContext(Dispatchers.IO) {
//            try {
//                eventsRepository.getAllEvents().toMutableList()
//            } catch (e: Exception) {
//                Log.e("getEventsByMonth", "An error occurred", e)
//                mutableListOf()  // Return an empty list or handle the error accordingly
//            }
//        }
//    }
//    fun getAllEvents(): MutableList<Event> {
//        var mutableList: MutableList<Event>
//        coroutineScope.launch(Dispatchers.IO) {
//            try{
//                eventsRepository.getAllEvents().collect { dbEvent ->
//                    mutableList = dbEvent.toMutableList()
//                }
//            } catch (e: Exception) {
//                Log.e("getEventsByMonth", "An error occurred")
//            }
//        }
//        return mutableList
//    }

//    fun getAllGames(): MutableList<Game> {
//
//    }
}
