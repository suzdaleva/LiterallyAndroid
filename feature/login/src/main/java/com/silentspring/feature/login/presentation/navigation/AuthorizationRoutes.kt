package com.silentspring.feature.login.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable data object Authorization

//Nested
@Serializable data object Credentials
@Serializable data object PrivacyPolicy
@Serializable data object ResetPassword
@Serializable data class Verification(val email: String)

