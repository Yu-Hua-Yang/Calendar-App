import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.ActivityScenario.launch
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.EventsRepository
import ca.project.calendarapp.data.Game
import ca.project.calendarapp.mock.MockEventDao
import ca.project.calendarapp.mock.MockOfflineEventRepository
import ca.project.calendarapp.ui.theme.CalendarViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.internal.wait
import org.junit.After
import kotlin.math.exp


@ExperimentalCoroutinesApi
class CalendarViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope()
    private val mockDao = MockEventDao()
    private val mockRepo = MockOfflineEventRepository(mockDao)

    private var event1 = Event(
        "Event1",
        "Location1",
        "Description1",
        "11/01/2023",
        "12:00",
        "13:00",
        "11",
        "2023",
        1,
        1
    )

    private var game1 = Game(
        "Tournament1",
        "Game1",
        "Genre1",
        "12",
        "100000",
        1
    )

    private var event2 = Event(
        "Event2",
        "Location2",
        "Description2",
        "11/02/2023",
        "13:00",
        "14:00",
        "12",
        "2023",
        2,
        2
    )

    private var game2 = Game(
        "Tournament2",
        "Game2",
        "Genre2",
        "24",
        "200000",
        2
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun stop() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun minusDayReturnCorrectDay() {
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Set The Initial Date
        calendarViewModel.setDay("12/16/2023")

        // Act
        calendarViewModel.minusDay()
        val expectedDay = "12/15/2023"
        val updatedDay = calendarViewModel.getDate()

        // Assert
        assertEquals(expectedDay, updatedDay)
    }

    @Test
    fun addDayReturnCorrectDay() {
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Set The Initial Date
        calendarViewModel.setDay("12/16/2023")

        // Act
        calendarViewModel.addDay()
        val expectedDay = "12/17/2023"
        val updatedDay = calendarViewModel.getDate()

        // Assert
        assertEquals(expectedDay, updatedDay)
    }

    @Test
    fun getDayReturnCorrectDay() {
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Set The Initial Date
        val expectedDate = "12/16/2023"
        calendarViewModel.setDay(expectedDate)

        // Act
        val resultDate = calendarViewModel.getDate()

        // Assert
        assertEquals(expectedDate, resultDate)
    }

    @Test
    fun setDayReturnCorrectDay() {
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Set The Initial Date
        calendarViewModel.setDay("12/16/2023")

        // Act
        val newDate = "12/17/2023"
        calendarViewModel.setDay(newDate)
        val resultDate = calendarViewModel.getDate()

        // Assert
        assertEquals(newDate, resultDate)
    }
//All BREAKING CAUSE IT ISNT BEING ADDED ON THE MOCK EVENT DAO
    @Test
    fun addEventAndGameInsertsCorrectly() = testDispatcher.runBlockingTest {
        // Setup ViewModel
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Act
        testScope.launch {
            calendarViewModel.testAddEvent(event1, game1)
//            calendarViewModel.testAddGame(game1)
        }
        delay(2000)
        var game = calendarViewModel.getCurrentGame()
        var event = calendarViewModel.getCurrent()
        // Assert
        assertEquals(game1.tournamentName, game.tournamentName)
        assertEquals(event1.title, event.title)
    }

    @Test
    fun updateEventAndGameUpdatesCorrectly() = testDispatcher.runBlockingTest {
        // Setup ViewModel
        val calendarViewModel = CalendarViewModel(mockRepo)
        // Act
        calendarViewModel.testAddEvent(event1, game1)
        delay(10000)
        var game = calendarViewModel.getCurrentGame()
        var event = calendarViewModel.getCurrent()
        val editEvent = Event(
            title = event2.title,
            location = event2.location,
            description = event2.description,
            eventDate = event.eventDate,
            startTime = event.startTime,
            endTime = event.endTime,
            month = event.month,
            year = event.year,
            gameId = event.gameId,
            id = event.id
        )
        val editGame = Game(
            tournamentName = game2.tournamentName,
            game = game2.game,
            genre = game2.genre,
            players = game2.players,
            prizePool = game.prizePool,
            id = game.id
        )
        calendarViewModel.updateEvent(editEvent, editGame)
        delay(10000)
        game = calendarViewModel.getCurrentGame()
        event = calendarViewModel.getCurrent()
        // Assert
        assertEquals(editEvent, event)
        assertEquals(editGame, game)
    }

    @Test
    fun deleteEventAndGameRemovesCorrectly() {
        // Setup ViewModel
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Act
        calendarViewModel.addEvent(event1, game1)
        var game = calendarViewModel.getCurrentGame()
        var event = calendarViewModel.getCurrent()
        calendarViewModel.deleteEvent(event, game)
//        delay(2000)
        var resultGame = calendarViewModel.getGameByGame(game1)
        var resultEvent = calendarViewModel.getEventByEvent(event1)

        // Assert
        assertNull(resultGame)
        assertNull(resultEvent)
    }


    @Test
    fun getEventByDayReturnCorrectly() = testDispatcher.runBlockingTest {
        // Setup ViewModel
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Act
        calendarViewModel.addEvent(event1, game1)
        delay(10000)
        var event1Result = calendarViewModel.getCurrent()
        calendarViewModel.getEventByDay(event1.eventDate)
        delay(10000)
        var events = calendarViewModel.getDayEvents()

        // Asserts
        assertEquals(event1Result, events[0])
    }

    @Test
    fun getEventByMonthReturnCorrectly() {
        // Setup ViewModel
        val calendarViewModel = CalendarViewModel(mockRepo)

        // Act
        calendarViewModel.addEvent(event1, game1)
        var event1Result = calendarViewModel.getCurrent()
        calendarViewModel.getEventsByMonth(event1.eventDate)
        var events = calendarViewModel.getMonthEvents()

        // Asserts
        assertEquals(event1Result, events[0])
    }
}
