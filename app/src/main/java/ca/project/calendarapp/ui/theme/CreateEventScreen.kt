package ca.project.calendarapp.ui.theme

import androidx.compose.material3.*
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.Game
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateEventScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    calendar: Calendar,
    onCreateButtonClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    calendarViewModel: CalendarViewModel

){
    fun areValuesValid(
        title: String,
        location: String,
        description: String,
        eventDate: String,
        startTime: String,
        endTime: String,
        tournamentName: String,
        gameName: String,
        genre: String,
        players: String,
        prizePool: String
    ): Boolean {
        return !(title == "" ||
                location == "" ||
                description == "" ||
                eventDate == "" ||
                startTime == "" ||
                endTime == "" ||
                tournamentName == "" ||
                gameName == "" ||
                genre == "" ||
                players == "" ||
                prizePool == "")
    }

    //Set time error phrase
    var errorMessage by remember { mutableStateOf("") }

    fun isTimeAvailable(listOfEvent: List<Event>, start: String, end: String): Boolean {
        for (event in listOfEvent) {
            if (start >= event.startTime && start <= event.endTime) {
                errorMessage = "Start time is overlapping between " + event.startTime + " and " + event.endTime
                return false
            }
            if (end >= event.startTime && end <= event.endTime) {
                errorMessage = "End time is overlapping between " + event.startTime + " and " + event.endTime
                return false
            }
            if (start < event.startTime && end > event.endTime) {
                errorMessage = "Event time is overlapping over " + event.startTime + " and " + event.endTime
                return false
            }
        }
        return true
    }

    fun isTimeInverse(start: String, end: String): Boolean {
        val (startHours, startMinutes) = start.split(":").let { (hour, minute) ->
            Pair(hour.toInt(), minute.toInt())
        }
        val (endHours, endMinutes) = end.split(":").let { (hour, minute) ->
            Pair(hour.toInt(), minute.toInt())
        }
        val midNightHour = "00"
        if (startHours > endHours) {
            if (endHours == midNightHour.toInt() && endMinutes == midNightHour.toInt()) {
                return true
            }
            return false
        } else if (startHours == endHours) {
            if (startMinutes > endMinutes) {
                return false
            } else if (startMinutes == endMinutes ){
                return false
            }
        }
        return true
    }
    var showError by remember { mutableStateOf(false) }
    var showErrorTime by remember { mutableStateOf(false) }
    var showErrorInverse by remember { mutableStateOf(false) }

    //Set default for event
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf(calendarViewModel.getDate()) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    //Get list of event for the date
    val calendarUiState by calendarViewModel.uiState.collectAsState()
    val eventList = calendarUiState.dayEvents

    //Set default for game
    var tournamentName by remember { mutableStateOf("") }
    var gameName by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var players by remember { mutableStateOf("") }
    var prizePool by remember { mutableStateOf("") }

    fun splitDate(date: String): List<String> {
        return date.split('/')
    }
    //Set this off default year
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val defaultDate = splitDate(eventDate)
        month = defaultDate[0]
        year = defaultDate[2]
        calendarViewModel.getEventByDay(eventDate)
    }

    val dateSelector = DateSelector(context, calendar){
        val dateSplit = splitDate(it)
        month = dateSplit[0]
        year = dateSplit[2]
        eventDate = it
        calendarViewModel.getEventByDay(it)
    }

    dateSelector.datePicker.minDate = calendar.timeInMillis

    val startTimeSelector = TimeSelector(context, calendar){
        startTime = it
    }

    val endTimeSelector = TimeSelector(context, calendar){
        endTime = it
    }

    Column(modifier = modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        BackButton {
            onBackButtonClicked()
        }
        TextFieldStringInput(textValue = "Title: ", value = title,
            onValueChange = {
                title = it
            }
        )
        TextFieldStringInput(textValue = "Location: ", value = location,
            onValueChange = {
                location = it
            }
        )
        TextFieldStringInput(textValue = "Description: ", value = description,
            onValueChange = {
                description = it
            }
        )

        //Figure how to fix to show the value in the text field
        //Also create a default date
        SelectorTextField("Date: ", dateSelector , eventDate)
        SelectorTextField("Start Time: ", startTimeSelector, startTime)
        SelectorTextField("End Time: ", endTimeSelector, endTime)

        TextFieldStringInput(textValue = "Tournament Name: ", value = tournamentName,
            onValueChange = {
               tournamentName = it
            }
        )

        TextFieldStringInput(textValue = "Game Name: ", value = gameName,
            onValueChange = {
                gameName = it
            }
        )

        TextFieldStringInput(textValue = "Genre: ", value = genre,
            onValueChange = {
                genre = it
            }
        )

        TextFieldNumberInput(textValue ="# Players: " , value = players,
            onValueChange = {
                players = it
            }
        )

        TextFieldNumberInput(textValue ="Prize Pool: " , value = prizePool,
            onValueChange = {
                prizePool = it
            }
        )

        if (showError) {
            ShowErrorDialog(
                onDismiss = {showError = false} ,
                title = "Missing Input",
                text = "One or Some of the fields is empty"
            )
        }

        if (showErrorTime) {
            ShowErrorDialog(
                onDismiss = {showErrorTime = false} ,
                title = "Invalid Time",
                text = errorMessage
            )
        }

        if (showErrorInverse) {
            ShowErrorDialog(
                onDismiss = {showErrorInverse = false},
                title = "Invalid time",
                text = "Start time is after End time"
            )
        }

        Button(
            onClick = {
                if (areValuesValid(title, location, description, tournamentName, eventDate, startTime, endTime, tournamentName, gameName, players, prizePool)) {
                    if (isTimeAvailable(eventList, startTime, endTime)) {
                        if (isTimeInverse(startTime, endTime)) {
                            val event = Event(
                                title,
                                location,
                                description,
                                eventDate,
                                startTime,
                                endTime,
                                month,
                                year,)

                            val game = Game(
                                tournamentName,
                                gameName,
                                genre,
                                players,
                                prizePool,
                            )

                            calendarViewModel.addEvent(event, game)
                            calendarViewModel.setDay(eventDate)
                            onCreateButtonClicked()
                        } else { showErrorInverse = true }
                    } else { showErrorTime = true }
                } else { showError = true }
            },
            modifier = modifier.padding(top = 16.dp)
        ) {
            Text("Create Event")
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun CreateEventScreenPreview() {
//    CalendarAppTheme {
//        CreateEventScreen(
//            onCreateButtonClicked = { },
//            onBackButtonClicked = { }
//        )
//    }
//}