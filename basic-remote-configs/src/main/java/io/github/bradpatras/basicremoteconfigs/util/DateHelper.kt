package io.github.bradpatras.basicremoteconfigs.util

import java.util.concurrent.TimeUnit

class DateHelper {
    companion object {
        fun now(): Long {
            return System.currentTimeMillis()
        }

        fun daysSince(timeMilliseconds: Long): Long {
            return TimeUnit.MILLISECONDS.toDays(now() - timeMilliseconds)
        }
    }
}