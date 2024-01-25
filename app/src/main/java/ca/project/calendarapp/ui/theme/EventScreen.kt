package ca.project.calendarapp.ui.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.CalendarScreen
import ca.project.calendarapp.R
import ca.project.calendarapp.ui.theme.theme.CalendarAppTheme

@Composable
fun EventViewScreen(
    onEditButtonClicked: () -> Unit= {},
    onBackButtonClicked: () -> Unit= {},
    onDeleteButtonClicked: () -> Unit= {},
    calendarViewModel: CalendarViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val calendarUiState by calendarViewModel.uiState.collectAsState()
    val event = calendarUiState.currentEvent
    val game = calendarUiState.currentGame
    val date = calendarUiState.date

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        BackButton {
            calendarViewModel.getEventByDay(date)
            onBackButtonClicked()
        }

        displayEventAndTournament(event = event, game = game)

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Edit button
            Button(
                onClick = onEditButtonClicked,
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text( text = stringResource(id = R.string.edit))
            }

            // Delete button
            Button(
                onClick = {
                    calendarViewModel.deleteEvent(event, game)
                    calendarViewModel.getEventByDay(date)
                    onDeleteButtonClicked()
                },
                modifier = modifier.padding(top = 16.dp)
            ) {
                Text("Delete")
            }
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun EventViewScreen() {
//    CalendarAppTheme {
//        EventViewScreen(
//            onEditButtonClicked = { },
//            onBackButtonClicked = { },
//        )
//    }
//}
