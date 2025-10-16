package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject


class ResetPasswordCodeVerificationUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {
    suspend operator fun invoke(email: String, resetPasswordCode: Int) {
        repository.verifyResetPasswordCode(email = email, resetPasswordCode = resetPasswordCode)
    }
}