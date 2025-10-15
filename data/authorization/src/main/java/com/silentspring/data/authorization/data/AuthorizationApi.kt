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
import com.silentspring.data.authorization.data.model.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface AuthorizationApi {
    @POST("/auth/register")
    suspend fun register(@Body request: RegistrationRequest)

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): TokenResponse

    @POST("/auth/verify")
    suspend fun verify(@Body request: VerificationRequest): TokenResponse

    @POST("/auth/verify-google-id-token")
    suspend fun verifyGoogleIdToken(@Body request: GoogleIdVerificationRequest): TokenResponse

    @POST("/auth/resend-code")
    suspend fun resendCode(@Body request: ResendCodeRequest)

    @POST("/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest)

    @POST("/auth/verify-reset-password-code")
    suspend fun verifyResetPasswordCode(@Body request: ResetPasswordVerificationRequest)

    @POST("/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): TokenResponse

    @POST("/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): TokenResponse
}