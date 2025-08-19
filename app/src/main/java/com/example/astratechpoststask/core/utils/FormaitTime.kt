package com.example.astratechpoststask.core.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatTimestamp(input: String): String {
    val zonedDateTime = ZonedDateTime.parse(input)
    val formatter = DateTimeFormatter.ofPattern("hh:mm a, yyyy-MM-dd")
    return zonedDateTime.format(formatter)
}