package com.example.weatherforecastapp.data.mapper

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun convertDateToDayOfWeek(dateString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.dayOfWeek.toString()
    }
}