package com.silentspring.feature.login.presentation.screens.verification.state

import androidx.annotation.StringRes

data class VerificationState(
    val code: String = "",
    @StringRes val codeSupportingTestRes: Int? = null,
    val isCodeError: Boolean = false,
    val isLoading: Boolean = false,
    val email: String = "",
    val timerText: String? = null
)