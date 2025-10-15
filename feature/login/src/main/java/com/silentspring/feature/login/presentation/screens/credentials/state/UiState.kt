package com.silentspring.feature.login.presentation.screens.credentials.state

import androidx.annotation.StringRes

data class CredentialsState(
    val username: String = "",
    @StringRes val usernameErrorRes: Int? = null,
    val email: String = "",
    @StringRes val emailErrorRes: Int? = null,
    val password: String = "",
    @StringRes val passwordErrorRes: Int? = null,
    val isPolicyChecked: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val type: AuthorizationType = AuthorizationType.SIGN_UP
) {
    fun isAuthEnabled(): Boolean {
        return if (type == AuthorizationType.SIGN_UP)
            username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() &&
                    usernameErrorRes == null && emailErrorRes == null && passwordErrorRes == null && isPolicyChecked && !isLoading
        else email.isNotEmpty() && password.isNotEmpty() && emailErrorRes == null && passwordErrorRes == null && !isLoading
    }
}