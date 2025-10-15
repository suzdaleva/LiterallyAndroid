package com.silentspring.core.common.exceptions

import com.silentspring.core.common.model.Error

data class NetworkException(
    val status: Int,
    val errors: List<Error>
) : Exception()