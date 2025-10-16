package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject


class ResetPasswordUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val updateTokensUseCase: UpdateTokensUseCase
) {
    suspend operator fun invoke(email: String, newPassword: String) {
        val result = authorizationRepository.resetPassword(email = email, newPassword = newPassword)
        updateTokensUseCase(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }
}