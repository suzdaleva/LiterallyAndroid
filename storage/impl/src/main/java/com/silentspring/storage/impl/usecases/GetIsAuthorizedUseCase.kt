package com.silentspring.storage.impl.usecases

import com.silentspring.storage.api.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetIsAuthorizedUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.userPreferences.map {
            it.isAuthorized
        }
    }
}