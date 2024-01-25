package ca.project.calendarapp.mock

import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.EventsDao
import ca.project.calendarapp.data.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MockEventDao : EventsDao {
    private val events = MutableStateFlow<MutableList<Event>>(mutableListOf())
    private val games = MutableStateFlow<MutableList<Game>>(mutableListOf())

    override fun getAllEvents(): Flow<List<Event>> = events

    override fun getEvent(id: Int): Flow<Event> =
        events.map { it.find { event -> event.id == id }!! }

    override fun getEventsByDay(date: String): Flow<List<Event>> =
        events.map { it.filter { event -> event.eventDate == date } }

    override fun getEventByGameId(gameId: Int): Flow<Event> {
        return events.map {list ->
            list.find { it.gameId == gameId } ?: Event()
        }
    }

    override fun getGameByGameId(gameId: Int): Flow<Game> =
        games.map { it.find { game -> game.id == gameId }!! }


    override fun getAllGames(): Flow<List<Game>> = games

    override fun getEventByEvent(
        date: String,
        start: String,
        end: String,
        title: String,
        location: String,
        description: String
    ): Flow<Event> {
        return events.map {
            it.find { event ->
                event.eventDate == date &&
                event.startTime == start &&
                event.endTime == end &&
                event.title == title &&
                event.location == location &&
                event.description == description
            } ?: Event()
        }
    }

    override fun getGameByGame(
        tournamentName: String,
        game: String,
        genre: String,
        players: String,
        prizePool: String
    ): Flow<Game> =
        games.map {
            println("Inside Map")
            it.find { ggame ->
                ggame.tournamentName == tournamentName
                ggame.game == game &&
                ggame.genre == genre &&
                ggame.players == players &&
                ggame.prizePool == prizePool
            } ?: Game()
        }

    override fun getEventsByMonth(month: String, year: String): Flow<List<Event>> =
        events.map { it.filter { event -> event.month == month && event.year == year } }


    override suspend fun insert(event: Event) {
        runBlocking {
            launch {
                events.value.add(event)
            }
        }
    }

    override suspend fun update(event: Event) {
        runBlocking {
            launch {
                events.value.map { if (it.id == event.id) event else it }
            }
        }
    }

    override suspend fun delete(event: Event) {
        runBlocking {
            launch {
                events.value.remove(event)
            }
        }
    }

    override suspend fun insertGame(game: Game) {
        runBlocking {
            launch {
                games.value.add(game)
            }
        }
    }

    override suspend fun updateGame(game: Game) {
        runBlocking {
            launch {
                games.value.map { if (it.id == game.id) game else it }
            }
        }
    }

    override suspend fun deleteGame(game: Game) {
        runBlocking {
            launch {
                games.value.remove(game)
            }
        }
    }
}