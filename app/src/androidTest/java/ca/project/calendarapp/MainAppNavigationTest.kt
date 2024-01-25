package ca.project.calendarapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import ca.project.calendarapp.ui.theme.CalendarViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.project.calendarapp.ui.theme.CalendarViewModelProvider


class MainAppNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController
    @Before
    fun setupCalendarNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            val calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModelProvider.Factory)
            MainApp(
                calendarViewModel = calendarViewModel,
                navController = navController,
                modifier = Modifier
            )
        }
    }
    @Test
    fun calendarNavHost_verifyStartDestination(){
        navController.assertCurrentRouteName(CalendarScreen.Month.name)
    }
    @Test
    fun navigate_to_day_from_month_view(){
        composeTestRule.onNodeWithText("1")
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Day.name)
    }

    @Test
    fun navigate_to_create_from_month_view(){
        composeTestRule.onNodeWithText("+")
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Create.name)
    }

    @Test
    fun navigate_to_create_from_day_view() {
        navigateToDayScreen()
        composeTestRule.onNodeWithContentDescription("Floating action button")
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Create.name)
    }

    @Test
    fun from_day_view_click_back_button() {
        navigateToDayScreen()
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
            navController.assertCurrentRouteName(CalendarScreen.Month.name)
        }
    }

    private fun navigateToCreateScreen() {
        composeTestRule.onNodeWithText("+")
            .performClick()
    }
    private fun navigateToDayScreen() {
        composeTestRule.onNodeWithText("1")
            .performClick()
    }
}

class EventNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    //I mocked and create another App is just so I can test it because the
    //month view was not done and ready to be used
    @Before
    fun setupEventNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            TestEventApp(navController = navController)
        }
    }

    @Test
    fun eventAppNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(CalendarScreen.Event.name)
    }

    @Test
    fun eventAppNavHost_verifyBackNavigationExistEventScreen() {
        val backText = composeTestRule.activity.getString(R.string.back)
        composeTestRule.onNodeWithText(backText).assertExists()
    }

    @Test
    fun eventNavHost_clickBackEvent_navigatesToDayScreen() {
        val backText = composeTestRule.activity.getString(R.string.back)
        composeTestRule.onNodeWithText(backText)
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Day.name)
    }

    @Test
    fun eventNavHost_clickEditEvent_navigatesToEditScreen() {
        val editText = composeTestRule.activity.getString(R.string.edit)
        composeTestRule.onNodeWithText(editText)
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Edit.name)
    }

    @Test
    fun editNavHost_clickBackEdit_navigatesToEventScreen() {
        navigateToEditScreen()
        val backText = composeTestRule.activity.getString(R.string.back)
        composeTestRule.onNodeWithText(backText)
            .performClick()
        navController.assertCurrentRouteName(CalendarScreen.Event.name)
    }

    private fun navigateToEditScreen() {
        val editText = composeTestRule.activity.getString(R.string.edit)
        composeTestRule.onNodeWithText(editText)
            .performClick()
    }
}
