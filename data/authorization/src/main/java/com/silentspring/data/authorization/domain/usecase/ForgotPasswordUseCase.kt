package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ForgotPasswordUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {
    suspend operator fun invoke(email: String) {
        repository.forgotPassword(email = email)
    }
}