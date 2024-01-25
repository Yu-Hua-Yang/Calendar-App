package ca.project.calendarapp.fake

import ca.project.calendarapp.network.Current
import ca.project.calendarapp.network.CurrentUnits
import ca.project.calendarapp.network.Daily
import ca.project.calendarapp.network.DailyUnits
import ca.project.calendarapp.network.Weather

object FakeWeatherSource {
    val weather: Weather = Weather(
        0.0,
        0.0,
        23.5,
        4.5,
        "fake",
        "fake",
        23.0,
        CurrentUnits(
            "fake",
            "fake",
            "fake",
            "fake"
        ),
        Current(
            "fake",
            30.4,
            32.1,
            0.0
        ),
        DailyUnits(
            "fake",
            "fake",
            "fake",
            "fake",
            "fake"
        ),
        Daily(
            listOf(),
            listOf(),
            listOf(),
            listOf(),
            listOf()
        )
    )
}