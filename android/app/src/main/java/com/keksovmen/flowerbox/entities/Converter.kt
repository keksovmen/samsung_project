package com.keksovmen.flowerbox.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun arrayOfIntsToStr(data: ArrayList<Int?>?): String {
        return gson.toJson(data)
    }

    @TypeConverter
    fun strToArrayOfInts(str: String?): ArrayList<Int> {
        return gson.fromJson(str, token)
    }

    companion object {
        private val gson = Gson()
        private val token: TypeToken<ArrayList<Int>> = object : TypeToken<ArrayList<Int>>() {}
    }
}
