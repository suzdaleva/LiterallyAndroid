package com.silentspring.core.common.model

data class Error(
    val field: String? = null,
    val reasonCode: Int,
    val message: String
)