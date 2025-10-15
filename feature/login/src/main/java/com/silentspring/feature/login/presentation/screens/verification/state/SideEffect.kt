package com.silentspring.feature.login.presentation.screens.verification.state

import androidx.annotation.StringRes

sealed interface SideEffect {
    data object CommonError : SideEffect
    data object VerificationSuccess : SideEffect
    data class ShowMessage(@StringRes val messageRes: Int) : SideEffect
}