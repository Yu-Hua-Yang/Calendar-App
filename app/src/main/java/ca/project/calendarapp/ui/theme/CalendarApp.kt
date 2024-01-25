package ca.project.calendarapp.ui.theme

import android.app.Application
import ca.project.calendarapp.data.AppContainer
import ca.project.calendarapp.data.AppDataContainer

class CalendarApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}