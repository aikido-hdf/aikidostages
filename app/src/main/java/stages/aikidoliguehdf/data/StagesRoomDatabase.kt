package stages.aikidoliguehdf.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


const val DATABASE_NAME = "StagesDatabase"
const val DATABASE_VERSION =1
@Database(entities = [Stages::class,Categories::class,Places::class,StagesCatMap::class, Favorites::class, StagesFavMap::class], version = 1)

abstract class StagesRoomDatabase : RoomDatabase() {

    abstract fun stagesDao(): StagesDao

    companion object {
        @Volatile
        private var instance: StagesRoomDatabase? = null
        fun getInstance(context: Context): StagesRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,StagesRoomDatabase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries()

                    .build()
            }
            return instance as StagesRoomDatabase
        }
    }
}