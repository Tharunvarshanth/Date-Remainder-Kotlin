package com.tvk.dateremainder.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tvk.dateremainder.model.ScheduleEntity


@Database(entities = [ScheduleEntity::class], version = 1, exportSchema = false)
 abstract class ScheduleRoomDatabase : RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDAO

   /* private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.scheduleDao()

                    // Delete all content here.



                }
            }
        }
    }*/

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ScheduleRoomDatabase? = null

        fun getDatabase(context: Context): ScheduleRoomDatabase {

            Log.d("ScheduleRoom DB","Passed")
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
             synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleRoomDatabase::class.java,
                    "dateremainder_database"
                ).build()
                INSTANCE = instance
                 return instance

            }
        }


    }
}