package com.example.weatherforecastapp.data

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Format {

    fun formatTimeFromEpoch(timeEpoch: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeEpoch
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    fun formatTimeByHour(time: String): String {
        return time.split(":")[0]

    }

    fun formatDate(epochTime: Long): String {
        val dateFormat = SimpleDateFormat("EEEE, d MMM", Locale.ENGLISH)
        val date = Date(epochTime * 1000)
        return dateFormat.format(date)
    }

    fun formatTimeLocation(localtime: String): String {
        return localtime.split(" ")[1]

    }

    fun convertTo24HourFormat(time12Hour: String): String {
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(time12Hour)
        return outputFormat.format(date)
    }

}