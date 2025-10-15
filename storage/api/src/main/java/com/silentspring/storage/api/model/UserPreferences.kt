package com.silentspring.storage.api.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val accessToken: String = "",
    val refreshToken: String = "",
    val isAuthorized: Boolean = false
)