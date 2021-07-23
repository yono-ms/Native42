package com.example.native42

import android.text.format.DateFormat
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

fun Date.toBestString(): String {
    kotlin.runCatching {
        val locale = Locale.getDefault()
        val cal = Calendar.getInstance(locale).apply { time = this@toBestString }
        val pattern = DateFormat.getBestDateTimePattern(locale, "yyyyMMMdEEEHHmmss")
        SimpleDateFormat(pattern, locale).format(cal.time)
    }.onSuccess {
        return it
    }.onFailure {
        val logger = LoggerFactory.getLogger("DateExtension")
        logger.error("SimpleDateFormat parse", it)
        return it.message ?: ""
    }
    return ""
}