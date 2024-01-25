package ca.project.calendarapp.mock

import ca.project.calendarapp.data.Event
import ca.project.calendarapp.data.EventsRepository
import ca.project.calendarapp.data.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MockOfflineEventRepository(private val mockEventDao: MockEventDao) : EventsRepository {
    override fun getEventsByDayStream(date: String): Flow<List<Event>> = mockEventDao.getEventsByDay(date)

    override fun getEventsByMonthStream(month: String, year: String): Flow<List<Event>> = mockEventDao.getEventsByMonth(month, year)

    override fun getEventStream(id: Int): Flow<Event> = mockEventDao.getEvent(id)

    override fun getEventThatMatchStream(
        date: String,
        start: String,
        end: String,
        title: String,
        location: String,
        description: String
    ): Flow<Event> =
        mockEventDao.getEventByEvent(date, start, end, title, location, description)

    override suspend fun insertEvent(item: Event) {
        mockEventDao.insert(item)
    }

    override suspend fun deleteEvent(item: Event) {
        mockEventDao.delete(item)
    }

    override suspend fun updateEvent(item: Event) {
        mockEventDao.update(item)
    }

    override suspend fun insertGame(item: Game) {
        mockEventDao.insertGame(item)
    }

    override suspend fun deleteGame(item: Game) {
        mockEventDao.deleteGame(item)
    }

    override suspend fun updateGame(item: Game) {
        mockEventDao.updateGame(item)
    }

    override fun getGameByGameIdStream(gameId: Int): Flow<Game> = mockEventDao.getGameByGameId(gameId)

    override fun getGameThatMatchStream(
        tournamentName: String,
        game: String,
        genre: String,
        players: String,
        prizePool: String
    ): Flow<Game> =
        mockEventDao.getGameByGame(tournamentName, game, genre, players, prizePool)

    override fun getEventByGameIdStream(gameId: Int): Flow<Event> = mockEventDao.getEventByGameId(gameId)

    override fun getAllEvents(): Flow<List<Event>> = mockEventDao.getAllEvents()

    override fun getAllGames(): Flow<List<Game>> = mockEventDao.getAllGames()

}