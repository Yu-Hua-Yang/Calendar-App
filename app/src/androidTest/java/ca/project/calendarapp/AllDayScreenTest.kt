package ca.project.calendarapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.mock.MockEventDao
import ca.project.calendarapp.mock.MockOfflineEventRepository
import ca.project.calendarapp.ui.theme.CalendarViewModel
import ca.project.calendarapp.ui.theme.CalendarViewModelProvider
import ca.project.calendarapp.ui.theme.DayPage
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AllDayScreenTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun setup() {
        rule.setContent {
            val calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModelProvider.Factory)
            DayPage(calendarViewModel = calendarViewModel,
                    onAddEventButtonClicked = { },
                    onEventButtonClicked = { },
                    reload = { },
                    onBackButton = { },
                    lat = latitude,
                    long = longitude
            )
        }
    }

    @Test
    fun testAddDay(){
        val dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        rule.onNodeWithText(LocalDateTime.now().format(dateTimeFormatter))
        rule.onNodeWithContentDescription("left arrow").performClick()
        rule.onNodeWithText(LocalDateTime.now().minusDays(1).format(dateTimeFormatter)).assertExists()
    }

    @Test
    fun testMinusDay(){
        val dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
        rule.onNodeWithText(LocalDateTime.now().format(dateTimeFormatter))
        rule.onNodeWithContentDescription("right arrow").performClick()
        rule.onNodeWithText(LocalDateTime.now().plusDays(1).format(dateTimeFormatter)).assertExists()
    }
}