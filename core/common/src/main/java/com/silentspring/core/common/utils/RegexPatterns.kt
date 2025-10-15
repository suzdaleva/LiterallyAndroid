package com.silentspring.core.common.utils

object RegexPatterns {
    const val EMAIL_PATTERN =
        "^(?=.{1,64}@)[A-Za-z0-9+_-]+(.[A-Za-z0-9+_-]+)*@[^-][A-Za-z0-9+-]+(.[A-Za-z0-9+-]+)*(.[A-Za-z]{2,})$"
    const val PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}$"
}