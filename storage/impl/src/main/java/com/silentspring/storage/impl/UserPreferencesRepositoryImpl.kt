package com.silentspring.storage.impl

import androidx.datastore.core.DataStore
import com.silentspring.storage.api.model.UserPreferences
import com.silentspring.storage.api.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<UserPreferences>
) : UserPreferencesRepository {

    override val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            exception.printStackTrace()
            UserPreferences()
        }
        .distinctUntilChanged()
        .flowOn(Dispatchers.IO)

    override suspend fun updateUserPreferences(transform: suspend (UserPreferences) -> UserPreferences) {
        dataStore.updateData { userPreferences ->
            transform(userPreferences)
        }
    }

    override suspend fun getUserPreferencesInstance() =
        dataStore.data.firstOrNull() ?: UserPreferences()
}