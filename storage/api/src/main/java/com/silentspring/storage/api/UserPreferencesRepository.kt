package com.silentspring.storage.api

import com.silentspring.storage.api.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun updateUserPreferences(transform: suspend (currentPreferences: UserPreferences) -> UserPreferences)
    suspend fun getUserPreferencesInstance(): UserPreferences
}