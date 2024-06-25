package com.tarun.shreebhagvatgita.DataSource.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.Instant

@Database(entities = [SavedChapter::class, SavedVerse::class], version = 2, exportSchema = false)
@TypeConverters(AppTypeConverters::class)

abstract class AppDatabase:RoomDatabase() {

    abstract fun savedchapter(): SavedChapterDao
    abstract fun savedverses(): SavedVerseDao



    companion object{
        @Volatile
        var Instance: AppDatabase?= null

        fun getDatabase(context: Context): AppDatabase?{
            var tempInstance = Instance
            if(Instance != null) return tempInstance
            else{
                synchronized(this){
                    val roomDb= Room.databaseBuilder(context, AppDatabase::class.java,"AppDatabase").fallbackToDestructiveMigration().build()
                    Instance =roomDb
                    return roomDb

                }
            }
        }
    }


}