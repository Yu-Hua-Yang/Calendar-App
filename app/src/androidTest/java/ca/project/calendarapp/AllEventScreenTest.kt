package ca.project.calendarapp



import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.Game
import ca.project.calendarapp.ui.theme.BackButton
import ca.project.calendarapp.ui.theme.TextFieldNumberInput
import ca.project.calendarapp.ui.theme.TextFieldStringInput
import ca.project.calendarapp.ui.theme.displayEventAndTournament
import org.junit.Rule
import org.junit.Test

class AllEventScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Create Event Ui Tests
    @Test
    fun backButtonRendersCorrectly() {
        composeTestRule.setContent {
           BackButton(
               onBackButtonClicked = {}
           )
        }

        composeTestRule.onNodeWithText("Back").assertExists()
    }

   @Test
   fun textFieldStringInputRendersCorrectly() {
       composeTestRule.setContent {
           TextFieldStringInput(
               textValue = "Title",
               value = "Title Field",
               onValueChange = {})
       }

       composeTestRule.onNodeWithText("Title").assertIsDisplayed()
       composeTestRule.onNodeWithText("Title Field").assertIsDisplayed()
   }

    @Test
    fun textFieldNumberInputRendersCorrectly() {
        composeTestRule.setContent {
            TextFieldNumberInput(
                textValue = "Players",
                value = "12",
                onValueChange = {})
        }

        composeTestRule.onNodeWithText("Players").assertIsDisplayed()
        composeTestRule.onNodeWithText("12").assertIsDisplayed()
    }

    @Test
    fun displayAllInformationRendersCorrectly() {
        val event = Event(
            "Event",
            "Location",
            "Description",
            "11/01/2023",
            "12:00",
            "13:00",
            "11",
            "2023",
        )

        val game = Game(
            "Tournament",
            "Game",
            "Genre",
            "12",
            "100000",
        )
        composeTestRule.setContent {
            displayEventAndTournament(
                event = event,
                game = game)
        }

        composeTestRule.onNodeWithText("EVENT DETAILS").assertIsDisplayed()
        composeTestRule.onNodeWithText("Title: Event").assertIsDisplayed()
        composeTestRule.onNodeWithText("Location: Location").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description: Description").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event Date: 11/01/2023").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start Time: 12:00").assertIsDisplayed()
        composeTestRule.onNodeWithText("End Time: 13:00").assertIsDisplayed()

        composeTestRule.onNodeWithText("TOURNAMENT DETAILS").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tournament Name: Tournament").assertIsDisplayed()
        composeTestRule.onNodeWithText("Game: Game").assertIsDisplayed()
        composeTestRule.onNodeWithText("Genre: Genre").assertIsDisplayed()
        composeTestRule.onNodeWithText("Players: 12").assertIsDisplayed()
        composeTestRule.onNodeWithText("Prize Pool: 100000$").assertIsDisplayed()
    }
}
