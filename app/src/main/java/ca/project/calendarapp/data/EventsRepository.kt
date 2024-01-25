package ca.project.calendarapp.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface EventsRepository {
    /**
     * Retrieve all the events from the the given data source that matches with [date].
     */
    fun getEventsByDayStream(date: String): Flow<List<Event>>

    /**
     * Retrieve all the events from the the given data source that matches with [month] [year].
     */
    fun getEventsByMonthStream(month: String, year: String): Flow<List<Event>>

    /**
     * Retrieve an event from the given data source that matches with the [id].
     */
    fun getEventStream(id: Int): Flow<Event>

    /**
     * Retrieve an event from the given data source that matches with the [date] [start] [end] [title] [location] [description].
     */
    fun getEventThatMatchStream(date: String, start: String, end: String, title: String, location: String, description: String): Flow<Event>

    /**
     * Insert event in the data source
     */
    suspend fun insertEvent(item: Event)

    /**
     * Delete event from the data source
     */
    suspend fun deleteEvent(item: Event)

    /**
     * Update event in the data source
     */
    suspend fun updateEvent(item: Event)

    /**
     * Insert game in the data source
     */
    suspend fun insertGame(item: Game)

    /**
     * Delete game from the data source
     */
    suspend fun deleteGame(item: Game)

    /**
     * Update game in the data source
     */
    suspend fun updateGame(item: Game)

    /**
     * Retrieve a game from the given data source that matches with the [gameId]
     */
    fun getGameByGameIdStream(gameId: Int): Flow<Game>

    /**
     * Retrieve a game from the given data source that matches with the [tournamentName] [game] [genre] [players] [prizePool]
     */
    fun getGameThatMatchStream(tournamentName: String, game: String, genre: String, players: String, prizePool: String): Flow<Game>

    /**
     * Retrieve an event from the given data source that matches with the [gameId]
     */
    fun getEventByGameIdStream(gameId: Int): Flow<Event>

    /**
     * Retrieve all events
     */
    fun getAllEvents(): Flow<List<Event>>

    /**
     * Retrieve all games
     */
    fun getAllGames(): Flow<List<Game>>
}