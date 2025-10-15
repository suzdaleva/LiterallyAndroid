package com.silentspring.data.authorization.utils

import com.silentspring.data.authorization.domain.AuthorizationRepository
import com.silentspring.storage.api.UserPreferencesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

internal class TokenValidationAuthenticator @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authorizationRepository: AuthorizationRepository
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            return runBlocking(Dispatchers.IO) {

                val userPreferences = userPreferencesRepository.userPreferences.firstOrNull()

                val currentAccessToken = userPreferences?.accessToken

                val refreshToken = userPreferences?.refreshToken ?: run {
                    userPreferencesRepository.updateUserPreferences { userPreferences ->
                        userPreferences.copy(
                            accessToken = "",
                            refreshToken = "",
                            isAuthorized = false
                        )
                    }
                    return@runBlocking null
                }

                // If the access token changed since the first failed request, retry with new token
                if (currentAccessToken != response.request.header("Authorization")
                        ?.removePrefix("Bearer ")
                ) {
                    return@runBlocking response.request.newBuilder()
                        .header("Authorization", "Bearer $currentAccessToken")
                        .build()
                }


                val newTokens = try {
                    authorizationRepository.refreshToken(refreshToken)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                    userPreferencesRepository.updateUserPreferences { userPreferences ->
                        userPreferences.copy(
                            accessToken = "",
                            refreshToken = "",
                            isAuthorized = false
                        )
                    }
                    return@runBlocking null
                }

                userPreferencesRepository.updateUserPreferences { userPreferences ->
                    userPreferences.copy(
                        accessToken = newTokens.accessToken,
                        refreshToken = newTokens.refreshToken
                    )
                }

                // Retry the original request with new token
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            }
        }
    }
}