package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerifyGoogleIdTokenUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val updateTokensUseCase: UpdateTokensUseCase
) {
    suspend operator fun invoke(idToken: String) {
        val result = authorizationRepository.verifyGoogleIdToken(idToken = idToken)
        updateTokensUseCase(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }
}