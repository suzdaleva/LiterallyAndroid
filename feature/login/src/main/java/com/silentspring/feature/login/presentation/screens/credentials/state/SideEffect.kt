package com.silentspring.feature.login.presentation.screens.credentials.state

import androidx.annotation.StringRes

sealed interface SideEffect {
    data object CommonError : SideEffect
    data object LoginSuccess : SideEffect
  //  data object LoginError : SideEffect
    data class ShowMessage(@StringRes val messageRes: Int) : SideEffect
 //   data object RegisteredWithPasswordError : SideEffect
    data class NotVerifiedError(val email: String) : SideEffect
    data class RegistrationSuccess(val email: String) : SideEffect
}