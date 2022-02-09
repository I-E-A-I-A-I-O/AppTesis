package com.justdance.apptesis.room

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun listFromString(value: String): List<String> = Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun stringFromList(value: List<String>): String = Gson().toJson(value)
}