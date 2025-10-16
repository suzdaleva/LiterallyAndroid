package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject


class ResendCodeUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {
    suspend operator fun invoke(email: String) {
        repository.resendCode(email = email)
    }
}