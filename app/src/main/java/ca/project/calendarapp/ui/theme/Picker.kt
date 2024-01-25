package ca.project.calendarapp.ui.theme

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

@Composable
fun DateSelector(context: Context = LocalContext.current,
                 calendar: Calendar,
                 onDateChange: (String) -> Unit) : DatePickerDialog {
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val day = calendar[Calendar.DAY_OF_MONTH]

    val dateSelector = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            onDateChange("${selectedMonth + 1}/$selectedDay/$selectedYear")
        }, year, month, day
    )

    Column(modifier = Modifier) {
        Text(text = "")
    }

    return dateSelector
}

@Composable
fun TimeSelector(context: Context = LocalContext.current,
                 calendar: Calendar,
                 onTimeChange: (String) -> Unit) :  TimePickerDialog {
    val hours = calendar[Calendar.HOUR_OF_DAY]
    val minutes = calendar[Calendar.MINUTE]

    val timeSelector = TimePickerDialog(
        context,
        { _, selectedHours: Int, selectedMinutes: Int ->
            onTimeChange("$selectedHours:${fixTheMinute(selectedMinutes)}")
        }, hours, minutes, true
    )
    
    Column(modifier = Modifier) {
        Text(text = "")
    }

    return timeSelector
}

fun fixTheMinute(minutes: Int) : String{
    if (minutes < 10){
        return minutes.toString().padStart(2, '0')
    }
    return minutes.toString()
}

@Composable
fun SelectorTextField(type: String, picker: AlertDialog, value: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween){
        Text(text = "$type",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier
                .padding(top = 5.dp, end =10.dp)
        )
        TextField(
            value = value,
            onValueChange = { },
            enabled = false,
            modifier = Modifier
                .width(250.dp)
                .height(59.dp)
                .padding(end = 30.dp)
                .clickable { picker.show() }
        )
    }
}