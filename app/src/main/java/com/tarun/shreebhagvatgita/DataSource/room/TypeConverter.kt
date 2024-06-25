package com.tarun.shreebhagvatgita.DataSource.room

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppTypeConverters {

    @TypeConverter
    fun fromListToString(List:List<String>):String{
        return Gson().toJson(List)
    }

    @TypeConverter
    fun fromStringToList(string:String):List<String>{
        return Gson().fromJson(string,object : TypeToken<List<String>>(){}.type)
    }
}