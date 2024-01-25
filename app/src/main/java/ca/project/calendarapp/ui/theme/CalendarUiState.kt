package ca.project.calendarapp.ui.theme

import androidx.compose.runtime.mutableStateOf
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.network.Current
import ca.project.calendarapp.network.CurrentUnits
import ca.project.calendarapp.network.Daily
import ca.project.calendarapp.network.DailyUnits
import ca.project.calendarapp.network.Weather
import ca.project.calendarapp.data.Game

data class CalendarUiState(
    var allEvents: MutableList<Event> = mutableListOf(),
    var monthEvents: MutableList<Event> = mutableListOf(),
    var dayEvents: MutableList<Event> = mutableListOf(),
    var date: String = "12/18/2023",
    var currentEvent: Event = Event(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    ),
    var weather: Weather = Weather(
        0.0,
        0.0,
        23.5,
        4.5,
        "time",
        "abbrev",
        23.0,
        CurrentUnits(
            "time",
            "interval",
            "temp",
            "precipitation"
        ),
        Current(
            "time",
            30.4,
            32.1,
            0.0
        ),
        DailyUnits(
            "time",
            "weather",
            "max",
            "min",
            "percentage"
        ),
        Daily(
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf()
        )
    ),
    var currentGame: Game = Game(
        "",
        "",
        "",
        "",
        ""
    ),
    var holidYear: Int = 0,
    var holidCountryCode : String = "",

)
