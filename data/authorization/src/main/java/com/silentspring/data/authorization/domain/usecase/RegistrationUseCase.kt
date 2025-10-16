package com.silentspring.data.authorization.domain.usecase

import com.silentspring.data.authorization.domain.AuthorizationRepository
import javax.inject.Inject


class RegistrationUseCase @Inject constructor(
    private val repository: AuthorizationRepository
) {
    suspend operator fun invoke(username: String, email: String, password: String) {
        repository.register(
            username = username,
            email = email,
            password = password
        )
    }
}