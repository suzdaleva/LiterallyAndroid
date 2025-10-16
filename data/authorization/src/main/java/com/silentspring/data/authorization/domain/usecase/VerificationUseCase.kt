package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject

class VerificationUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val updateTokensUseCase: UpdateTokensUseCase
) {
    suspend operator fun invoke(email: String, verificationCode: Int) {
        val result =
            authorizationRepository.verify(email = email, verificationCode = verificationCode)
        updateTokensUseCase(
            accessToken = result.accessToken, refreshToken = result.refreshToken
        )
    }
}