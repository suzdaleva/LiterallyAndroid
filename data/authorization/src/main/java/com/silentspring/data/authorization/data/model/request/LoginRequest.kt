package com.silentspring.data.authorization.data.model.request

internal data class LoginRequest(
    val email: String,
    val password: String
)