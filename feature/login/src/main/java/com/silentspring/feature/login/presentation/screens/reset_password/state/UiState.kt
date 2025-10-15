package com.silentspring.feature.login.presentation.screens.reset_password.state

import androidx.annotation.StringRes
import com.silentspring.feature.login.R


enum class ResetPasswordStep(@StringRes val titleRes: Int) {
    EMAIL(titleRes = R.string.forgot_password),
    CODE(titleRes = R.string.check_email_title),
    NEW_PASSWORD(titleRes = R.string.reset_password_title)
}

data class ResetPasswordState(
    val email: String = "",
    @StringRes val emailErrorRes: Int? = null,
    val resetPasswordCode: String = "",
    @StringRes val resetPasswordCodeErrorRes: Int? = null,
    val newPassword: String = "",
    @StringRes val newPasswordErrorRes: Int? = null,
    val confirmPassword: String = "",
    @StringRes val confirmPasswordErrorRes: Int? = null,
    val timerText: String? = null,
    val isLoading: Boolean = false,
    val step: ResetPasswordStep = ResetPasswordStep.EMAIL
)