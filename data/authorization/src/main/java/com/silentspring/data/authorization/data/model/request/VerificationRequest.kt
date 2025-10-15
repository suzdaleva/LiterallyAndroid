package com.silentspring.data.authorization.data.model.request

internal data class VerificationRequest(
    val email: String,
    val verificationCode: Int
)