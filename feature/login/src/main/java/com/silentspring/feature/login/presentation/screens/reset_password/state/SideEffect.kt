package com.silentspring.feature.login.presentation.screens.reset_password.state

import androidx.annotation.StringRes

sealed interface SideEffect {
    data object CommonError : SideEffect
    data object ResetPasswordSuccess : SideEffect
    data class ShowMessage(@StringRes val massageRes: Int) : SideEffect
}