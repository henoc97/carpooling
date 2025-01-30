package tg.ulcrsandroid.carpooling.application.utils.managers

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

object TimeManager {

    fun convertTimestampToFormattedDate(timestamp: Long): String {
        val simpleDate = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val currentDate = simpleDate.format(Date(timestamp))
        return currentDate
    }
}