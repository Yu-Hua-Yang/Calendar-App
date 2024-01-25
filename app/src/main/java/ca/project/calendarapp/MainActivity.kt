package ca.project.calendarapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ca.project.calendarapp.data.InventoryDatabase
import ca.project.calendarapp.data.OfflineEventsRepository
import ca.project.calendarapp.ui.theme.CalendarViewModel
import ca.project.calendarapp.ui.theme.CalendarViewModelProvider
import ca.project.calendarapp.ui.theme.CreateEventScreen
import ca.project.calendarapp.ui.theme.DayPage
import ca.project.calendarapp.ui.theme.EditEventScreen
import ca.project.calendarapp.ui.theme.EventViewScreen
import ca.project.calendarapp.ui.theme.MonthPage
import ca.project.calendarapp.ui.theme.theme.CalendarAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Calendar

enum class CalendarScreen(@StringRes val title: Int) {
    Month(title = R.string.month),
    Day(title = R.string.day),
    Event(title = R.string.event),
    Edit(title = R.string.edit),
    Create(title = R.string.create),
}

var latitude: Double = 0.0
var longitude: Double = 0.0
private lateinit var fusedLocationClient: FusedLocationProviderClient


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location != null){
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        super.onCreate(savedInstanceState)
        setContent {
            CalendarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    val db = InventoryDatabase.getDatabase(applicationContext)
//                    val eventDao = db.eventDao()
//                    offlineEventsRepository = OfflineEventsRepository(eventDao)
                    MainApp()
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainApp(
    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModelProvider.Factory),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    //calendarViewModel.getWeatherFromApi(latitude, longitude)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CalendarScreen.valueOf(
        backStackEntry?.destination?.route ?: CalendarScreen.Month.name
    )
    Scaffold(

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CalendarScreen.Month.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = CalendarScreen.Month.name) {
                MonthPage(
                    onCreateButtonClicked = { navController.navigate(CalendarScreen.Create.name) },
                    onDayButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
            composable(route = CalendarScreen.Day.name) {
                DayPage(
                    calendarViewModel = calendarViewModel,
                    onAddEventButtonClicked = { navController.navigate(CalendarScreen.Create.name) },
                    onEventButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    reload = { navController.navigate(CalendarScreen.Day.name) },
                    onBackButton = { navController.navigate(CalendarScreen.Month.name) },
                    lat = latitude,
                    long = longitude,
                    modifier = modifier,
                )
            }
            composable(route = CalendarScreen.Event.name) {
                EventViewScreen(
                    onEditButtonClicked = { navController.navigate(CalendarScreen.Edit.name) },
                    onBackButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    onDeleteButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }

            composable(route = CalendarScreen.Edit.name) {
                EditEventScreen(
                    calendar = Calendar.getInstance(),
                    onSaveButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
            //Pass a calendar
            composable(route = CalendarScreen.Create.name) {
                CreateEventScreen(
                    calendar = Calendar.getInstance(),
                    onCreateButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    onBackButtonClicked = { navController.navigate(CalendarScreen.Month.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TestEventApp(
    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModelProvider.Factory),
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CalendarScreen.valueOf(
        backStackEntry?.destination?.route ?: CalendarScreen.Event.name
    )
    Scaffold(

    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = CalendarScreen.Event.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = CalendarScreen.Month.name) {
                MonthPage(
                    onCreateButtonClicked = { navController.navigate(CalendarScreen.Create.name) },
                    onDayButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
            composable(route = CalendarScreen.Day.name) {
                DayPage(
                    calendarViewModel = calendarViewModel,
                    onAddEventButtonClicked = { navController.navigate(CalendarScreen.Create.name) },
                    onEventButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    reload = { navController.navigate(CalendarScreen.Day.name) },
                    onBackButton = { navController.navigate(CalendarScreen.Month.name) },
                    lat = latitude,
                    long = longitude,
                    modifier = modifier
                )
            }
            composable(route = CalendarScreen.Event.name) {
                EventViewScreen(
                    onEditButtonClicked = { navController.navigate(CalendarScreen.Edit.name) },
                    onBackButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    onDeleteButtonClicked = { navController.navigate(CalendarScreen.Day.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }

            composable(route = CalendarScreen.Edit.name) {
                EditEventScreen(
                    calendar = Calendar.getInstance(),
                    onSaveButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
            //Pass a calendar
            composable(route = CalendarScreen.Create.name) {
                CreateEventScreen(
                    calendar = Calendar.getInstance(),
                    onCreateButtonClicked = { navController.navigate(CalendarScreen.Event.name) },
                    onBackButtonClicked = { navController.navigate(CalendarScreen.Month.name) },
                    calendarViewModel = calendarViewModel,
                    modifier = modifier
                )
            }
        }
    }

}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CalendarAppTheme {
//        MainApp()
//    }
//}