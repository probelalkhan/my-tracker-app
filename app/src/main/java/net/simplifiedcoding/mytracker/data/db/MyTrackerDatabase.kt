package net.simplifiedcoding.mytracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Survey::class, Coordinate::class],
    version = 1
)
abstract class MyTrackerDatabase : RoomDatabase() {

    abstract fun getSurveyDao(): SurveyDao
    abstract fun getCoordinateDao(): CoordinateDao

    companion object {
        @Volatile
        private var instance: MyTrackerDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyTrackerDatabase::class.java,
                "MyTracker.db"
            ).build()
    }
}