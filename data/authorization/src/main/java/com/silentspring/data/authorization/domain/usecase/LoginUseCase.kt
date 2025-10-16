package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val updateTokensUseCase: UpdateTokensUseCase
) {
    suspend operator fun invoke(email: String, password: String) {
        val result = authorizationRepository.login(
            email = email,
            password = password
        )
        updateTokensUseCase(
            accessToken = result.accessToken,
            refreshToken = result.refreshToken
        )
    }
}