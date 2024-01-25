package ca.project.calendarapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.project.calendarapp.R
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.Game

@Composable
fun TextFieldStringInput(
    textValue: String,
    value: String,
    onValueChange: (String) -> Unit
){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            text = textValue,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(top = 5.dp, end =10.dp)
        )
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .width(250.dp)
                .height(59.dp)
                .padding(end = 30.dp)
                .testTag(textValue)
        )
    }
}

@Composable
fun TextFieldNumberInput(
    textValue: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            text = textValue,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(top = 5.dp, end =10.dp)
        )
        TextField(
            value = value,
            onValueChange = {
                if (it.matches(Regex("^\\d*\$"))) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .width(250.dp)
                .height(59.dp)
                .padding(end = 30.dp)
                .testTag(textValue),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun BackButton(onBackButtonClicked: () -> Unit) {
    Button(
        onClick = {
            onBackButtonClicked()
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text( text = stringResource(id = R.string.back))
    }
}

@Composable
fun displayEventAndTournament(event: Event, game: Game) {
    //Event Details
    Text(text = "EVENT DETAILS", fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
    Text(text = "Title: ${event.title}")
    Text(text = "Location: ${event.location}")
    Text(text = "Description: ${event.description}")
    Text(text = "Event Date: ${event.eventDate}")
    Text(text = "Start Time: ${event.startTime}")
    Text(text = "End Time: ${event.endTime}")

    //Game Details
    Text(text = "TOURNAMENT DETAILS", fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
    Text(text = "Tournament Name: ${game.tournamentName}")
    Text(text = "Game: ${game.game}")
    Text(text = "Genre: ${game.genre}")
    Text(text = "Players: ${game.players}")
    Text(text = "Prize Pool: ${game.prizePool}$")
}

@Composable
fun ShowErrorDialog(
    onDismiss: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text("OK")
            }
        }
    )
}