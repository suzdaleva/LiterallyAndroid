package com.silentspring.core.network.model

import  com.silentspring.core.common.model.Error

data class ErrorResponse(
    val timestamp: String,
    val errors: List<Error>?
)