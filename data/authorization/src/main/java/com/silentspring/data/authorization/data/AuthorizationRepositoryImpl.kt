package com.silentspring.data.authorization.data

import com.silentspring.data.authorization.data.model.request.ForgotPasswordRequest
import com.silentspring.data.authorization.data.model.request.GoogleIdVerificationRequest
import com.silentspring.data.authorization.data.model.request.LoginRequest
import com.silentspring.data.authorization.data.model.request.RefreshRequest
import com.silentspring.data.authorization.data.model.request.RegistrationRequest
import com.silentspring.data.authorization.data.model.request.ResendCodeRequest
import com.silentspring.data.authorization.data.model.request.ResetPasswordRequest
import com.silentspring.data.authorization.data.model.request.ResetPasswordVerificationRequest
import com.silentspring.data.authorization.data.model.request.VerificationRequest
import com.silentspring.data.authorization.domain.AuthorizationRepository
import com.silentspring.data.authorization.domain.model.TokenBusiness
import com.silentspring.data.authorization.data.model.response.toBusiness
import javax.inject.Inject


internal class AuthorizationRepositoryImpl @Inject constructor(
    private val api: AuthorizationApi
) : AuthorizationRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String
    ) {
        api.register(
            RegistrationRequest(
                username = username,
                email = email,
                password = password
            )
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): TokenBusiness {
        return api.login(
            LoginRequest(
                email = email,
                password = password
            )
        ).toBusiness()
    }

    override suspend fun verify(
        email: String,
        verificationCode: Int
    ): TokenBusiness {
        return api.verify(
            VerificationRequest(
                email = email,
                verificationCode = verificationCode
            )
        ).toBusiness()
    }

    override suspend fun verifyGoogleIdToken(idToken: String): TokenBusiness {
        return api.verifyGoogleIdToken(
            GoogleIdVerificationRequest(
                idToken = idToken
            )
        ).toBusiness()
    }

    override suspend fun resetPassword(
        email: String,
        newPassword: String
    ): TokenBusiness {
        return api.resetPassword(
            ResetPasswordRequest(
                email = email,
                newPassword = newPassword
            )
        ).toBusiness()
    }

    override suspend fun refreshToken(refreshToken: String): TokenBusiness {
        return api.refreshToken(
            RefreshRequest(
                refreshToken = refreshToken
            )
        ).toBusiness()
    }

    override suspend fun resendCode(email: String) {
        api.resendCode(
            ResendCodeRequest(
                email = email
            )
        )
    }

    override suspend fun forgotPassword(email: String) {
        api.forgotPassword(
            ForgotPasswordRequest(
                email = email
            )
        )
    }

    override suspend fun verifyResetPasswordCode(
        email: String,
        resetPasswordCode: Int
    ) {
        api.verifyResetPasswordCode(
            ResetPasswordVerificationRequest(
                email = email,
                resetPasswordCode = resetPasswordCode
            )
        )
    }
}