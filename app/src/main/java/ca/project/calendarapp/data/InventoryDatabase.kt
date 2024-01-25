package ca.project.calendarapp.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Event::class, Game::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InventoryDatabase : RoomDatabase() {
    abstract fun eventDao(): EventsDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        private val MIGRATION_1_3: Migration = object : Migration(1, 4) {
            override fun migrate(database: SupportSQLiteDatabase){
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `games_new` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`tournamentName` TEXT NOT NULL, " +
                            "`game` TEXT NOT NULL, " +
                            "`genre` TEXT NOT NULL, " +
                            "`players` TEXT NOT NULL, " +
                            "`prizePool` TEXT NOT NULL, " +
                            ")"
                )

                // Copy data from the old table to the new one
                database.execSQL("INSERT INTO `games_new` SELECT * FROM `games`")

                // Drop the old table
                database.execSQL("DROP TABLE `games`")

                // Rename the new table to the correct name
                database.execSQL("ALTER TABLE `games_new` RENAME TO `games`")

                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `events_new` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`title` TEXT NOT NULL, " +
                            "`location` TEXT NOT NULL, " +
                            "`description` TEXT NOT NULL, " +
                            "`eventDate` TEXT NOT NULL, " +
                            "`startTime` TEXT NOT NULL, " +
                            "`endTime` TEXT NOT NULL, " +
                            "`month` TEXT NOT NULL, " +
                            "`year` TEXT NOT NULL, " +
                            "`gameId` INTEGER NOT NULL, " +
                            "FOREIGN KEY(`gameId`) REFERENCES `games`(`id`) ON DELETE CASCADE" +
                            ")"
                )

                // Copy data from the old table to the new one
                database.execSQL("INSERT INTO `events_new` SELECT * FROM `events`")

                // Drop the old table
                database.execSQL("DROP TABLE `events`")

                // Rename the new table to the correct name
                database.execSQL("ALTER TABLE `events_new` RENAME TO `events`")

            }
        }

        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "events_database")
                    .addMigrations(MIGRATION_1_3)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
