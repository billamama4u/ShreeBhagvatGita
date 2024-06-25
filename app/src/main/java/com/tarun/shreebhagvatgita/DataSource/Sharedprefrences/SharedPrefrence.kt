package com.tarun.shreebhagvatgita.DataSource.Sharedprefrences

import android.content.Context
import android.content.SharedPreferences
import androidx.transition.Visibility.Mode

class SharedPrefrenceManager(val context: Context)  {

    val sharedPrefrence : SharedPreferences by lazy {
        context.getSharedPreferences("saved_chapter",Context.MODE_PRIVATE)
    }

    fun getAllChapter(): Set<String>{
        return sharedPrefrence.all.keys
    }

    fun addChapter(key:String,value : Int){
        sharedPrefrence.edit().putInt(key,value).apply()
    }
    fun removeChapter(key: String){
        sharedPrefrence.edit().remove(key).apply()
    }

    val verseSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("saved_verse",Context.MODE_PRIVATE)
    }

    fun getAllVerse(): Set<String>{
        return verseSharedPreferences.all.keys
    }

    fun addVerse(key:String,value : Int){
        verseSharedPreferences.edit().putInt(key,value).apply()
    }
    fun removeverse(key: String){
        verseSharedPreferences.edit().remove(key).apply()
    }
}