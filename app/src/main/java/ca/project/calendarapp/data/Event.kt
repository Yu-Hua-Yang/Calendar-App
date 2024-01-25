package ca.project.calendarapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

//this will be changed to Event
//Change for ID to be auto generated
@Entity(tableName = "events", foreignKeys = [ForeignKey(entity = Game::class,
    childColumns = ["gameId"],
    parentColumns = ["id"],
    onDelete = ForeignKey.CASCADE)],
    indices = [Index("gameId")]
)
@TypeConverters(Converters::class)
data class Event(
    var title: String = "",
    var location: String = "",
    var description: String = "",
    var eventDate: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var month: String = "",
    var year: String = "",
    @ColumnInfo(name = "gameId") var gameId: Int = 0,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
    )