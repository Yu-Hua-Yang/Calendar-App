package ca.project.calendarapp.ui.theme

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.R
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.Game
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditEventScreen(
    context: Context = LocalContext.current,
    onSaveButtonClicked: () -> Unit= {},
    calendar: Calendar= Calendar.getInstance(),
    calendarViewModel: CalendarViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
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

    fun isTimeAvailable(listOfEvent: List<Event>, start: String, end: String, id: Int): Boolean {
        for (event in listOfEvent) {
            if (event.id != id){
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

    val calendarUiState by calendarViewModel.uiState.collectAsState()

    //List of Events
    val eventList = calendarUiState.dayEvents

    //Event
    var editedEvent by remember { mutableStateOf(calendarUiState.currentEvent) }
    var eventDate by remember { mutableStateOf(editedEvent.eventDate) }
    var startTime by remember { mutableStateOf(editedEvent.startTime) }
    var endTime by remember { mutableStateOf(editedEvent.endTime) }

    //Game
    var editedGame by remember { mutableStateOf(calendarUiState.currentGame)}

    val dateSelector = DateSelector(context, calendar){
        var dateSplit = it.split('/')
        editedEvent.copy(month = dateSplit[0])
        editedEvent.copy(year = dateSplit[2])
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
            onSaveButtonClicked()
        }

        TextFieldStringInput(textValue = "Title: ", value = editedEvent.title,
            onValueChange = {
                editedEvent = editedEvent.copy(title = it)
            })
        TextFieldStringInput(textValue = "Location: ", value = editedEvent.location,
            onValueChange = {
                editedEvent = editedEvent.copy(location = it)
            })
        TextFieldStringInput(textValue = "Description: ", value = editedEvent.description,
            onValueChange = {
                editedEvent = editedEvent.copy(description = it)
            })

        SelectorTextField("Date: ", dateSelector , eventDate)
        SelectorTextField("Start Time: ", startTimeSelector, startTime)
        SelectorTextField("End Time: ", endTimeSelector, endTime)

        TextFieldStringInput(textValue = "Tournament Name: ", value = editedGame.tournamentName,
            onValueChange = {
                editedGame = editedGame.copy(tournamentName = it)
            })
        TextFieldStringInput(textValue = "Game: ", value = editedGame.game,
            onValueChange = {
                editedGame = editedGame.copy(game = it)
            })
        TextFieldStringInput(textValue = "Genre: ", value = editedGame.genre,
            onValueChange = {
                editedGame = editedGame.copy(genre = it)
            })
        TextFieldNumberInput(textValue = "# Player: ", value = editedGame.players,
            onValueChange = {
                editedGame = editedGame.copy(players = it)
            })

        TextFieldNumberInput(textValue = "Prize Pool: ", value = editedGame.prizePool,
            onValueChange = {
                editedGame = editedGame.copy(prizePool = it)
            })

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
                if (areValuesValid(editedEvent.title, editedEvent.location,
                        editedEvent.description, editedEvent.eventDate,
                        editedEvent.startTime, editedEvent.endTime, editedGame.tournamentName,
                        editedGame.game, editedGame.genre, editedGame.players, editedGame.prizePool)) {
                    if (isTimeAvailable(eventList, startTime, endTime, editedEvent.id)) {
                        if (isTimeInverse(startTime, endTime)) {
                            val updateEvent = Event(
                                editedEvent.title,
                                editedEvent.location,
                                editedEvent.description,
                                eventDate,
                                startTime,
                                endTime,
                                editedEvent.month,
                                editedEvent.year,
                                editedEvent.gameId,
                                editedEvent.id,
                            )

                            val updatedGame = Game(
                                editedGame.tournamentName,
                                editedGame.game,
                                editedGame.genre,
                                editedGame.players,
                                editedGame.prizePool,
                                editedGame.id
                            )
                            calendarViewModel.updateEvent(updateEvent, updatedGame)
                            onSaveButtonClicked()
                        } else { showErrorInverse = true }
                    } else { showErrorTime = true }
                } else { showError = true }
            },
            modifier = modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun EditEventScreenPreview() {
//    CalendarAppTheme {
//        EditEventScreen(onSaveButtonClicked = { })
//    }
//}