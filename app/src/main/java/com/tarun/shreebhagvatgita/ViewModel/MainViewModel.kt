package com.tarun.shreebhagvatgita.ViewModel

import android.app.Application

import android.util.Log
import android.util.TypedValue

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tarun.shreebhagvatgita.DataSource.Api.ApiUtilities
import com.tarun.shreebhagvatgita.DataSource.Sharedprefrences.SharedPrefrenceManager
import com.tarun.shreebhagvatgita.Repository.AppRepository
import com.tarun.shreebhagvatgita.models.VerseItems
import com.tarun.shreebhagvatgita.DataSource.room.AppDatabase
import com.tarun.shreebhagvatgita.DataSource.room.SavedChapter
import com.tarun.shreebhagvatgita.DataSource.room.SavedVerse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val Dao = AppDatabase.getDatabase(application)?.savedchapter()
    private val verseDao= AppDatabase.getDatabase(application)?.savedverses()
    private val repository = AppRepository()
    val chapter = repository.chapters
    val sharedPreferences = SharedPrefrenceManager(application)
    val verseSharedPreferences = SharedPrefrenceManager(application)

    init {
        fetchChapters()
    }

    private fun fetchChapters() {
        repository.fetchChapters()
    }

    private val _verses = MutableLiveData<List<VerseItems>>()
    val verses: LiveData<List<VerseItems>> get() = _verses

    fun fetchVerses(chapterNumber: Int) {
        Log.d("MainViewModel", "Fetching verses for chapter $chapterNumber")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val verseList = ApiUtilities.api.getVerses(chapterNumber)
                Log.d("MainViewModel", "Fetched ${verseList.size} verses")
                withContext(Dispatchers.Main) {
                    _verses.value = verseList
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching verses: ${e.message}")
            }
        }
    }

    fun getVerse(chapterNumber: Int, verseNumber: Int): LiveData<VerseItems> {
        return repository.fetchVerse(chapterNumber, verseNumber)
    }

    suspend fun inserChapter(savedChapter: SavedChapter) = Dao?.insertSavedChapter(savedChapter)
    fun getAllSavedChapters() = Dao?.getAllSavedChapters()
    suspend fun deleteSavedChapter(id: Int) = Dao?.deleteSavedChapter(id)
    fun getSavedChapter(chapterNumber: Int) = Dao?.getSavedChapter(chapterNumber)
    suspend fun insertSavedVerse(savedVerse: SavedVerse) =verseDao?.insertSavedVerse(savedVerse)
    fun getAllSavedVerses() = verseDao?.getAllSavedVerses()
    suspend fun deleteSavedVerse(chapterNumber: Int,verseNumber: Int) = verseDao?.deleteSavedVerse(chapterNumber,verseNumber)
    fun getSavedVerse(chapterNumber: Int, verseNumber: Int) = verseDao?.getSavedVerse(chapterNumber,verseNumber)
    suspend fun addToSharedPrefrence(Key: String, value: Int) = sharedPreferences.addChapter(Key, value)
    suspend fun removechpsp(key:String)=sharedPreferences.removeChapter(key)
    fun getchpsp(): Set<String> =sharedPreferences.getAllChapter()
    suspend fun addToVerseSharedPrefrence(Key: String, value: Int) = verseSharedPreferences.addChapter(Key, value)
    suspend fun removeversechpsp(key:String)=verseSharedPreferences.removeChapter(key)
    fun getversechpsp(): Set<String> =verseSharedPreferences.getAllChapter()

}






