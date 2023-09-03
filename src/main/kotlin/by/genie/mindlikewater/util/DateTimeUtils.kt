package by.genie.mindlikewater.util

import java.time.OffsetDateTime

fun isWithinLastMonth(time: OffsetDateTime): Boolean {
    return time.isAfter(OffsetDateTime.now().minusDays(30))
}

fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60 % 60
    val seconds = totalSeconds % 60
    return StringBuilder()
        .append(totalSeconds / 3600)
        .append(":")
        .append(if (minutes < 10) "0" else "")
        .append(minutes)
        .append(":")
        .append(if (seconds < 10) "0" else "")
        .append(seconds)
        .toString()
}
