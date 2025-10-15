package com.silentspring.data.authorization.domain

import com.silentspring.data.authorization.domain.model.TokenBusiness

interface AuthorizationRepository {
    suspend fun register(username: String, email: String, password: String)
    suspend fun login(email: String, password: String): TokenBusiness
    suspend fun verify(email: String, verificationCode: Int): TokenBusiness
    suspend fun verifyGoogleIdToken(idToken: String): TokenBusiness
    suspend fun resetPassword(email: String, newPassword: String): TokenBusiness
    suspend fun refreshToken(refreshToken: String): TokenBusiness
    suspend fun resendCode(email: String)
    suspend fun forgotPassword(email: String)
    suspend fun verifyResetPasswordCode(email: String, resetPasswordCode: Int)
}