package io.github.bradpatras.basicremoteconfigs.util

import java.time.LocalDateTime
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class DateHelper {
    companion object {
        fun dateTimeFromEpochMillis(long: Long): LocalDateTime {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(long), ZoneId.of("UTC"))
        }

        fun now(): LocalDateTime {
            return LocalDateTime.now(ZoneId.of("UTC"))
        }

        fun daysSince(dateTime: LocalDateTime): Long {
            return ChronoUnit.DAYS.between(dateTime, now())
        }
    }
}