package com.silentspring.data.authorization.domain.usecase

import com.silentspring.storage.api.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpdateTokensUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(accessToken: String, refreshToken: String) {
        userPreferencesRepository.updateUserPreferences { userPreferences ->
            userPreferences.copy(
                accessToken = accessToken,
                refreshToken = refreshToken,
                isAuthorized = true
            )
        }
    }
}