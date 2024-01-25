package ca.project.calendarapp.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.text.DateFormatSymbols
import java.util.Calendar
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.ui.theme.theme.CalendarAppTheme
import java.text.SimpleDateFormat
import java.util.Locale


//Get all the days in a month
fun getMonthDays(year: Int, month: Int): Pair<Int, List<Int>> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    return firstDayOfWeek to (1..days).toList()
}

@Composable
fun MonthPage(
    onCreateButtonClicked : () -> Unit = {},
    onDayButtonClicked : () -> Unit = {},
    calendarViewModel: CalendarViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    var monthIndex by remember { mutableStateOf(0) }
    var year by remember { mutableStateOf(2024) }
    var date by remember { mutableStateOf(("")) }

    val (firstDayOfWeek, monthList) = getMonthDays(year, monthIndex)
    val displayMonths = DateFormatSymbols().months

    var weekDays = listOf<String>("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    var currentWeek = 1
    var numOfWeeks = 0

    //Fetch the holidays when the year or device code changes
    val deviceCountryCode = LocalContext.current.resources.configuration.locales[0].country

    calendarViewModel.setHolidayYear(year)
    calendarViewModel.setHolidayCountryCode(deviceCountryCode)
    calendarViewModel.getHolidayFromApi()

    val holidays = calendarViewModel.holidays




    // Get the number of days through first day of the week
    monthList.forEach{
        if ( it % 7 == 1){
            numOfWeeks += 1
        }
    }

    // Get date and events for month
    date = "${monthIndex + 1}/01/$year"
    calendarViewModel.getEventsByMonth(date)


    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Button(onClick = {
                if(monthIndex > 0){
                    monthIndex -= 1
                }
                else{
                    monthIndex = 11
                    year -= 1
                }


            }) {
                Text(text = "<")
            }
            Text(
                text = "${displayMonths[monthIndex]} $year",
                modifier = modifier.padding(4.dp)
            )

            Button(onClick = {
                if(monthIndex < displayMonths.size - 1){
                    monthIndex += 1
                }
                else{
                    monthIndex = 0
                    year += 1
                }

            }) {
                Text(text = ">")
            }
        }


        Spacer(modifier = modifier)

        // List the days of the week
        Row(
            modifier = modifier.fillMaxWidth()
        ){

            weekDays.forEach{
                Card(
                    modifier = modifier
                        .width(50.dp)
                        .height(40.dp)
                        .padding(3.dp)
                ) {
                    Text(
                        text = "" + it,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .fillMaxSize()
                            .background(Color.LightGray))
                }
            }
        }

        // For every week in month
        for (week in 1..numOfWeeks) {
            Row {
                // Calculate the starting day of the current week
                val startDay = (week - 1) * 7 - firstDayOfWeek + 2

                // For every day in a week
                for (day in 1..7) {
                    val n = startDay + day

                    if (n in 1..monthList.size) {
                        val isEventDay = calendarViewModel.getMonthEvents().any {
                            getDayFromDate(it.eventDate) == n
                        }

                        val isHoliday = holidays?.any {
                            getHolidayDay(it.date) == n && getHolidayMonth(it.date) == monthIndex + 1
                        } == true

                        val backgroundColor = if (isHoliday) Color.Red else Color.Blue

                        Card(
                            modifier = Modifier
                                .clickable {
                                    onDayButtonClicked()

                                    date = "${monthIndex + 1}/$n/$year"
                                    calendarViewModel.setDay(date)
                                    calendarViewModel.getEventByDay(date)
                                }
                                .size(50.dp)
                                .background(backgroundColor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "$n",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(if (isEventDay) Color.Yellow else Color.Transparent),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else{
                        Card(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.Blue)
                        ){
                            Text(
                                text=""
                            )
                        }
                    }
                }
            }
        }


        Spacer(modifier = modifier.padding(vertical = 4.dp))

        FloatingActionButton(
            onClick = {
                val defaultDate = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Calendar.getInstance().time)
                calendarViewModel.setDay(defaultDate)
                onCreateButtonClicked()
                      },
            modifier = modifier.padding(horizontal = 140.dp),

        ) {
            Text(
                text = "+",

            )
        }
    }
}

fun getDayFromDate(dateString: String): Int {
    val parts = dateString.split('/')

    if (parts.isNotEmpty()) {
        return parts[1].toIntOrNull() ?: 0
    }

    return 0

}

private fun getHolidayDay(date: String): Int {
    val parts = date.split('-')
    return parts.getOrNull(2)?.toIntOrNull() ?: 0
}

private fun getHolidayMonth(date: String): Int {
    val parts = date.split('-')
    return parts.getOrNull(1)?.toIntOrNull() ?: 0
}

@Preview(showBackground = true)
@Composable
fun MonthPreview() {
    CalendarAppTheme {
        MonthPage()
    }
}
