package ca.project.calendarapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.project.calendarapp.ui.theme.CalendarViewModelProvider
import ca.project.calendarapp.ui.theme.EventViewScreen
import ca.project.calendarapp.ui.theme.MonthPage
import ca.project.calendarapp.ui.theme.theme.CalendarAppTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MonthActivityTest  {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displayMonthArrows() {
        composeTestRule.setContent {
            MonthPage(
                onCreateButtonClicked = {},
                onDayButtonClicked = {},
            )
        }

        composeTestRule.onNodeWithText("<").assertExists()
        composeTestRule.onNodeWithText("<").performClick()

        composeTestRule.onNodeWithText(">").assertExists()
        composeTestRule.onNodeWithText(">").performClick()
    }

    @Test
    fun checkDisplayedButtons(){
        composeTestRule.setContent {
            MonthPage(
                onCreateButtonClicked = {},
                onDayButtonClicked = {},
            )
        }

        composeTestRule.onNodeWithText("1").assertExists()
        composeTestRule.onNodeWithText("5").assertExists()
    }

    @Test
    fun checkChangeMonth(){
        composeTestRule.setContent {
            MonthPage(
                onCreateButtonClicked = {},
                onDayButtonClicked = {},
            )
        }

        composeTestRule.onNodeWithText("January").assertExists()
        composeTestRule.onNodeWithText(">").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("February").assertExists()
        composeTestRule.onNodeWithText("<").performClick()
        composeTestRule.onNodeWithText("January").assertExists()

    }


    @Test
    fun checkDayOfWeek(){
        composeTestRule.setContent {
            MonthPage(
                onCreateButtonClicked = {},
                onDayButtonClicked = {},
            )
        }

        composeTestRule.onNodeWithText("Mon").assertExists()
        composeTestRule.onNodeWithText("Thu").assertExists()

        composeTestRule.onNodeWithText("<").performClick()
        composeTestRule.onNodeWithText("Mon").assertExists()
        composeTestRule.onNodeWithText("Thu").assertExists()

    }
}