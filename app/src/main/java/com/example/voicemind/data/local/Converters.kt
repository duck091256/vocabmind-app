package com.example.voicemind.data.local

import androidx.room3.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(",")

    @TypeConverter
    fun toList(str: String): List<String> =
        if (str.isBlank()) emptyList() else str.split(",")
}