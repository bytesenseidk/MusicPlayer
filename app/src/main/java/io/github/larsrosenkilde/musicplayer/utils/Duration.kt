package io.github.larsrosenkilde.musicplayer.utils

import java.util.concurrent.TimeUnit

object DurationFormatter {
    fun formatAsMS(ms: Int) = formatAsMS(ms.toLong())
    fun formatAsMS(ms: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MICROSECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MICROSECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1)
        )
    }
}