package ca.project.calendarapp.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(Event: Event)

    @Update
    suspend fun update(Event: Event)

    @Delete
    suspend fun delete(Event: Event)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(Game: Game)

    @Update
    suspend fun updateGame(Game: Game)

    @Delete
    suspend fun deleteGame(Game: Game)

    @Query("SELECT * from events WHERE id = :id")
    fun getEvent(id: Int): Flow<Event>

    @Query("SELECT * from events WHERE eventDate = :date")
    fun getEventsByDay(date: String): Flow<List<Event>>

    @Query("SELECT * from events WHERE month = :month AND year =:year")
    fun getEventsByMonth(month: String, year: String): Flow<List<Event>>

    @Query("SELECT * from events WHERE eventDate = :date " +
            "AND startTime = :start " +
            "AND endTime = :end " +
            "AND title = :title " +
            "AND location =:location " +
            "AND description = :description")
    fun getEventByEvent(date: String, start: String, end: String, title: String, location: String, description: String): Flow<Event>

    @Query("SELECT * from events WHERE gameId = :gameId")
    fun getEventByGameId(gameId: Int): Flow<Event>

    @Query("SELECT * from games WHERE id = :gameId")
    fun getGameByGameId(gameId: Int): Flow<Game>

    @Query("SELECT * from games WHERE tournamentName = :tournamentName " +
            "AND game = :game " +
            "AND genre = :genre " +
            "AND players = :players " +
            "AND prizePool = :prizePool")
    fun getGameByGame(tournamentName: String, game: String, genre: String, players: String, prizePool: String): Flow<Game>

    @Query("SELECT * from games")
    fun getAllGames(): Flow<List<Game>>

    @Query("SELECT * from events")
    fun getAllEvents(): Flow<List<Event>>
}