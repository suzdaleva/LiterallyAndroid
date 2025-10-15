package com.silentspring.data.authorization.data.model.request

internal data class ResetPasswordVerificationRequest(
    val email: String,
    val resetPasswordCode: Int
)
