package com.tarun.shreebhagvatgita.DataSource.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedChapter(savedChapter: SavedChapter)

    @Query("SELECT * FROM SavedChapter")
     fun getAllSavedChapters():LiveData<List<SavedChapter>>

    @Query("DELETE FROM SavedChapter WHERE id = :id")
    suspend fun deleteSavedChapter(id:Int)

    @Query("SELECT * FROM SavedChapter WHERE chapter_number= :chapterNumber")
     fun getSavedChapter(chapterNumber:Int):LiveData<SavedChapter>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedVerse(savedVerse: SavedVerse)

}

@Dao
interface SavedVerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedVerse(savedVerse: SavedVerse)

    @Query("SELECT * FROM SavedVerse")
    fun getAllSavedVerses():LiveData<List<SavedVerse>>

    @Query("DELETE FROM SavedVerse WHERE chapter_number= :chapterNumber AND verse_number= :verseNumber")
    suspend fun deleteSavedVerse(chapterNumber:Int,verseNumber:Int)

    @Query("SELECT * FROM SavedVerse WHERE chapter_number= :chapterNumber AND verse_number= :verseNumber")
    fun getSavedVerse(chapterNumber:Int,verseNumber:Int):LiveData<SavedVerse>


}

