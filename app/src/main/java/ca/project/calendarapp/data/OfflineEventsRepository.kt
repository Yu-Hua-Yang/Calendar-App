package ca.project.calendarapp.data
import kotlinx.coroutines.flow.Flow


class OfflineEventsRepository(private val EventsDao: EventsDao) : EventsRepository {
    override fun getEventsByDayStream(date: String): Flow<List<Event>> = EventsDao.getEventsByDay(date)
    override fun getEventsByMonthStream(month: String, year: String): Flow<List<Event>> = EventsDao.getEventsByMonth(month, year)

    override fun getEventThatMatchStream(
        date: String,
        start: String,
        end: String,
        title: String,
        location: String,
        description: String
    ): Flow<Event> = EventsDao.getEventByEvent(date, start, end, title, location, description)

    override fun getEventStream(id: Int): Flow<Event> = EventsDao.getEvent(id)

    override suspend fun insertEvent(Event: Event) = EventsDao.insert(Event)

    override suspend fun deleteEvent(Event: Event) = EventsDao.delete(Event)

    override suspend fun updateEvent(Event: Event) = EventsDao.update(Event)

    override suspend fun insertGame(Game: Game) = EventsDao.insertGame(Game)

    override suspend fun deleteGame(Game: Game) = EventsDao.deleteGame(Game)

    override suspend fun updateGame(Game: Game) = EventsDao.updateGame(Game)
    override fun getGameByGameIdStream(gameId: Int): Flow<Game>  = EventsDao.getGameByGameId(gameId)

    override fun getGameThatMatchStream(
        tournamentName: String,
        game: String,
        genre: String,
        players: String,
        prizePool: String
    ): Flow<Game> = EventsDao.getGameByGame(tournamentName, game, genre, players, prizePool)

    override fun getEventByGameIdStream(gameId: Int): Flow<Event> = EventsDao.getEventByGameId(gameId)
    override fun getAllEvents(): Flow<List<Event>> = EventsDao.getAllEvents()

    override fun getAllGames(): Flow<List<Game>> = EventsDao.getAllGames()
}