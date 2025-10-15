package com.silentspring.data.authorization.data.model.request

internal data class RegistrationRequest(
    val username: String,
    val email: String,
    val password: String
)
