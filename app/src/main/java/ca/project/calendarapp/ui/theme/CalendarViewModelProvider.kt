package ca.project.calendarapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object CalendarViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CalendarViewModel(calendarApp().container.itemsRepository)
        }
    }
}

fun CreationExtras.calendarApp(): CalendarApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CalendarApp)