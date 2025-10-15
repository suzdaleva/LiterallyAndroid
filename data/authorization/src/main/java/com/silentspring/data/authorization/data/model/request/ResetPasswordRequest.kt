package com.silentspring.data.authorization.data.model.request

internal data class ResetPasswordRequest(
    val email: String,
    val newPassword: String
)
