// icons
// sunny = https://www.flaticon.com/free-icons/sun
// thunder = https://www.flaticon.com/free-icons/storm
// snowy = https://www.flaticon.com/free-icons/snow
// rainy = https://www.flaticon.com/free-icons/rain
// cloudy = https://www.flaticon.com/free-icons/clouds
package ca.project.calendarapp.ui.theme

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.text.Layout
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.CalendarScreen
import ca.project.calendarapp.R
import ca.project.calendarapp.data.Event
import java.text.ParseException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayPage(
    calendarViewModel: CalendarViewModel = viewModel(),
    onEventButtonClicked : () -> Unit = {},
    onAddEventButtonClicked : () -> Unit = {},
    reload: () -> Unit = {},
    onBackButton : () -> Unit = {},
    lat: Double,
    long: Double,
    modifier: Modifier = Modifier
){
    calendarViewModel.getWeatherFromApi(lat, long)
    var date = calendarViewModel.getDate()
    Box {
        BackHandler(
            onBack = { onBackButton() }
        )
        Column {
            Head(
                reload = reload,
                date = date,
                calendarViewModel = calendarViewModel,
                modifier = modifier
            )

            val holidayForCurrentDay = calendarViewModel.holidays?.find { it.date == dateConvert(date) }
            Text(
                text = holidayForCurrentDay?.localName ?: "No holiday",
                modifier = Modifier.padding(8.dp)
            )

            HoursList(
                date = date,
                calendarViewModel = calendarViewModel,
                onEventButtonClicked = { onEventButtonClicked() },
                modifier = Modifier
            )
        }
        FloatingActionButton(
            onClick = { onAddEventButtonClicked() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Rounded.Add, "Floating action button")
        }
    }
}

fun dateConvert(inputDate: String): String {
    try {
        val inputFormat = SimpleDateFormat("M/d/yyyy")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")

        val date: Date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        return inputDate // return the original date in case of an error
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Head(
    reload: () -> Unit = {},
    date: String,
    calendarViewModel: CalendarViewModel,
    modifier : Modifier = Modifier
){
    Column {
        Row(
            modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray)
        ) {
            Icon(
                //onclick to change to prev day
                Icons.Rounded.KeyboardArrowLeft,
                contentDescription = "left arrow",
                modifier
                    .weight(1f)
                    .clickable {
                        calendarViewModel.minusDay()
                        reload()
                    }
            )
            Text(
                //date from view model
                text = date,
                modifier.weight(1f)
            )
            Icon(
                //onclick to change to next day
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = "right arrow",
                modifier
                    .weight(1f)
                    .clickable {
                        calendarViewModel.addDay()
                        reload()
                    }
            )
        }
        Row(
            modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray)
        ){
            val apiDate: String = calendarViewModel.transformDateForApi(date)
            val validDate: Int = calendarViewModel.checkIfDateIsWithinNextSevenDays(apiDate)
            if (validDate != -1){
                //check if date is the current day
                if (validDate in 0..6){
                    val icon = calendarViewModel.getWeatherIcon(validDate)
                    val draw = when (icon) {
                        "sun" -> R.drawable.sun
                        "cloud" -> R.drawable.cloud
                        "rain" -> R.drawable.rain
                        "snow" -> R.drawable.snow
                        "storm" -> R.drawable.storm
                        else -> R.drawable.sun
                    }
                    Row(
                        modifier
                            .height(40.dp)
                            .background(color = Color.LightGray)
                            .weight(1f)
                            .padding(start = 10.dp)
                    ){
                        if (validDate == 0){
                            Text(text = calendarViewModel.getCurrentTemperature() + "°C",
                            Modifier.padding(end = 10.dp))
                        }
                        Icon(
                            painter = painterResource(id = draw),
                            contentDescription = "icon",
                            Modifier.size(25.dp)
                        )
                    }
                }
                Row(
                    modifier
                        .height(40.dp)
                        .background(color = Color.LightGray)
                        .weight(1f)
                        .padding(start = 10.dp)
                ){
                    if (validDate == 0){
                        Text(text = calendarViewModel.getCurrentPrecipitationAmount() + "mm",
                            Modifier.padding(end = 10.dp))
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "PRCP: " + calendarViewModel.getPrecipitationPercentage(apiDate) + "%",
                    Modifier.padding(end = 10.dp))
                    Text(
                        text = "Max: " + calendarViewModel.getMaxTemp(apiDate) + " Min: " + calendarViewModel.getMinTemp(apiDate),
                        Modifier.padding(end = 10.dp)
                    )
                }
            }
        }
    }
}
data class EventMock(val title: String?, val startTime: LocalDateTime, val endTime: LocalDateTime)
// Composable function to display events in the day view
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HoursList(
    date: String,
    calendarViewModel: CalendarViewModel,
    onEventButtonClicked : () -> Unit = {},
    modifier: Modifier = Modifier
) {
    //mm/dd/yyyy
    calendarViewModel.getEventByDay(date)
    val eventsFromViewModel = calendarViewModel.getDayEvents()
    val events = mutableListOf<EventMock>()
    //turn events of string into event of localtimedate
    eventsFromViewModel.forEach{ evnt ->
        val startHour: Int?
        val startMinutes: Int?
        val endHour: Int?
        val endMinutes: Int?
        val splitStart = evnt.startTime?.split(':')
        startHour = splitStart?.get(0)?.toInt()
        startMinutes = splitStart?.get(1)?.toInt()
        val splitEnd = evnt.endTime?.split(':')
        endHour = splitEnd?.get(0)?.toInt()
        endMinutes = splitEnd?.get(1)?.toInt()
        val e = EventMock(evnt.title,
            LocalDateTime.now().withHour(startHour!!).withMinute(startMinutes!!),
            LocalDateTime.now().withHour(endHour!!).withMinute(endMinutes!!))
        events.add(e)
    }

    val state = rememberScrollState()
    val sortedEvents = events.sortedBy { it.startTime }
    val sdf = DateTimeFormatter.ofPattern("HH:mm")
    val timesOfDay: MutableList<LocalDateTime> = mutableListOf()
    val dayStart = LocalDateTime.now().withHour(0).withMinute(0)
    val dayEnd = LocalDateTime.now().withHour(23).withMinute(59)
    var currentTime = LocalDateTime.now().withHour(0).withMinute(0)

    //set up first column of timeslots
    while (currentTime.isBefore(dayEnd)) {
        val endTime = currentTime.plusHours(1)

        timesOfDay.add(currentTime)

        currentTime = endTime
    }
    var startPos: Float = 0F
    Box {
        LazyRow(
            modifier
                .verticalScroll(state)
                .height((24 * 60).toFloat().dp)
                .fillMaxWidth()
        ) {
            item {
                LazyColumn(
                    modifier = Modifier
                        .background(color = Color.LightGray)
                        .fillParentMaxWidth(0.15f)
                ) {
                    items(timesOfDay) { dateTime ->
                        Text(
                            text = dateTime.format(sdf),
                            modifier.height(60.dp)
                        )
                    }
                }
            }
            item {
                LazyColumn(
                    modifier = Modifier
                        .background(color = Color.LightGray)
                        .fillParentMaxWidth(0.85f)
                        .height((24 * 60).toFloat().dp)
                ) {
                    items(sortedEvents) { event ->
                        var pad = (((event.startTime.hour)
                            .toFloat() * 60) + (event.startTime.minute
                            .toFloat())) - (startPos)
                        var height = event.startTime.until(event.endTime, java.time.temporal.ChronoUnit.MINUTES)
                            .toFloat()


                        // the if statements here are to account for events that start during other events
                        if (pad + height > 0) {
                            if (pad <= 0) {
                                height = (pad + height)
                                pad = 0F
                            }

                            Button(
                                onClick = {
                                    //this makes sure the correct current event is set
                                    eventsFromViewModel.forEach { evnt ->
                                        if (evnt.title == event.title){
                                            calendarViewModel.setCurrent(evnt)

                                        }
                                    }
                                    onEventButtonClicked()
                                    },
                                shape = RectangleShape,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = pad.dp)
                                    .height(height.dp)
                            ) {
                                Text(
                                    text = event.title!!
                                )
                            }
                            startPos += (pad + height)
                        }
                    }
                }
            }
        }
    }

}

