package com.tarun.shreebhagvatgita.Repository

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tarun.shreebhagvatgita.DataSource.Api.ApiUtilities
import com.tarun.shreebhagvatgita.models.ChapterItems
import com.tarun.shreebhagvatgita.models.VerseItems
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import retrofit2.Callback

class AppRepository {

    private val _chapters = MutableLiveData<List<ChapterItems>>()
    val chapters: LiveData<List<ChapterItems>> get() = _chapters



    fun fetchChapters() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val chaptersList = ApiUtilities.api.getChapters()
                withContext(Dispatchers.Main) {
                    _chapters.value = chaptersList
                }
            } catch (e: Exception) {
                // Handle the exception, maybe update LiveData with an error state
                // For example: _chapters.postValue(emptyList()) or show an error message
            }
        }
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

    val _verse= MutableLiveData<VerseItems>()


    fun fetchVerse(chapterNumber: Int, verseNumber: Int):LiveData<VerseItems>{
        CoroutineScope(Dispatchers.Main).launch {
            val verse = ApiUtilities.api.getVerse(chapterNumber, verseNumber)
            withContext(Dispatchers.Main) {
                _verse.value = verse

            }
        }
        return _verse
    }
}

