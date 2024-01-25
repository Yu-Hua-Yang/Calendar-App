package ca.project.calendarapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.EventsDao
import ca.project.calendarapp.data.Game
import ca.project.calendarapp.data.InventoryDatabase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
   private lateinit var eventDao: EventsDao
   private lateinit var inventoryDatabase: InventoryDatabase

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
       1,
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
        2,
    )

    private var game2 = Game(
        "Tournament2",
        "Game2",
        "Genre2",
        "24",
        "200000",
        2
    )

    private suspend fun addEvent(event: Event) {
        eventDao.insert(event)
    }

    private suspend fun addGame(game: Game) {
        eventDao.insertGame(game)
    }

    private suspend fun deleteEvent(event: Event) {
        eventDao.delete(event)
    }

    private suspend fun deleteGame(game: Game) {
        eventDao.deleteGame(game)
    }

    private suspend fun updateEvent(event: Event) {
        eventDao.update(event)
    }

    private suspend fun updateGame(game: Game) {
        eventDao.updateGame(game)
    }

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        eventDao = inventoryDatabase.eventDao()
    }

    @Test
    @Throws(Exception::class)
    fun daoInserts_insertsGameIntoDB() = runBlocking {
        addGame(game1)
        val game = eventDao.getGameByGameId(game1.id)
        assertEquals(game, game1)
    }

    @Test
    @Throws(Exception::class)
    fun daoInserts_insertsEventIntoDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        val event = eventDao.getEvent(event1.id)
        assertEquals(event, event1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEventsByDay_returnsEventsFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        val event = eventDao.getEventsByDay("11/01/2023").first()
        assertEquals(event[0], event1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEventsByMonth_returnsEventsFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        addGame(game2)
        addEvent(event2)
        val events = eventDao.getEventsByMonth("11", "2023").first()
        assertEquals(events[0], event1)
        assertEquals(events[1], event2)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEventByEvent_returnsEventFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        val event = eventDao.getEventByEvent(
            event1.eventDate,
            event1.startTime,
            event1.endTime,
            event1.title,
            event1.location,
            event1.description
        )
        assertEquals(event, event1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetEventByGameId_returnsEventFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        val event = eventDao.getEventByGameId(event1.gameId)
        assertEquals(event, event1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetGameByGame_returnsGamesFromDB() = runBlocking {
        addGame(game1)
        val game = eventDao.getGameByGame(
            game1.tournamentName,
            game1.game,
            game1.genre,
            game1.players,
            game1.prizePool
        )
        assertEquals(game, game1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteEvent_deleteEventFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        var events = eventDao.getEventsByMonth(event1.month, event1.year).first()
        assertEquals(1, events.size)
        deleteEvent(event1)
        events = eventDao.getEventsByMonth(event1.month, event1.year).first()
        assertEquals(0, events.size)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteGame_deleteGameFromDB() = runBlocking {
        addGame(game1)
        var games = eventDao.getAllGames().first()
        assertEquals(1, games.size)
        deleteEvent(event1)
        games = eventDao.getAllGames().first()
        assertEquals(0, games.size)
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateEvent_updateEventFromDB() = runBlocking {
        addGame(game1)
        addEvent(event1)
        var event = eventDao.getEventsByDay("11/01/2023").first()
        assertEquals(1, event.size)
        assertEquals(event1, event[0])

        val editEvent1 = Event(
            title = "EditEvent",
            location = "EditLocation",
            description = "EditDescription",
            eventDate = event2.eventDate,
            startTime = event2.startTime,
            endTime = event2.endTime,
            month = event2.month,
            year = event2.year,
            gameId = event1.gameId,
            id = event1.id,
        )

        updateEvent(editEvent1)
        event = eventDao.getEventsByDay("11/01/2023").first()
        assertEquals(1, event.size)
        assertEquals(editEvent1, event[0])
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateGame_updateGameFromDB() = runBlocking {
        addGame(game1)
        var games = eventDao.getAllGames().first()
        assertEquals(1, games.size)
        assertEquals(game1, games[0])

        val editGame1 = Game(
            tournamentName = game2.tournamentName,
            game = game2.game,
            genre = game2.genre,
            players = game2.players,
            prizePool = game2.prizePool,
            id = game1.id
        )

        updateGame(editGame1)
        games = eventDao.getAllGames().first()
        assertEquals(1, games.size)
        assertEquals(editGame1, games[0])
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        inventoryDatabase.close()
    }
}