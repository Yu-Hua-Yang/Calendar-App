package ca.project.calendarapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    val tournamentName: String = "",
    val game: String = "",
    val genre: String = "",
    val players: String = "",
    val prizePool: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)