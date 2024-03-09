package com.example.weatherforecastapp.data

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Mapper {

    fun convertDateToDayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.toString()
    }
}