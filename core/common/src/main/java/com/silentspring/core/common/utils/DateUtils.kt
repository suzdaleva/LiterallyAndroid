package com.silentspring.core.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toTimeDateString(
    pattern: String = "HH:mm:ss dd/MM/yyyy"
): String {
    val dateTime = Date(this)
    val format = SimpleDateFormat(pattern, Locale.US)
    return format.format(dateTime)
}