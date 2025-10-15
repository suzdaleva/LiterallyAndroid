package com.silentspring.data.authorization.data

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.silentspring.data.authorization.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.util.Base64
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GoogleAuthManager @Inject constructor() {

    suspend fun signIn(
        context: Context,
        onError: () -> Unit = {},
        onPlayServicesError: () -> Unit = {},
        onSuccess: (GetCredentialResponse) -> Unit = {}
    ) {
        withContext(Dispatchers.IO) {

            val request = generateCredentialRequest(filterByAuthorizedAccounts = false)

            if (!checkGooglePlayServices(
                    context = context,
                    onError = onPlayServicesError
                )
            ) return@withContext

            val credentialManager = CredentialManager.Companion.create(context)

            try {

                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                onSuccess(result)

            } catch (exception: NoCredentialException) {
                signUp(context = context, onError = onError, onSuccess = onSuccess)
                Log.e(TAG, "No credentials found: $exception")

            } catch (throwable: Throwable) {
                onError()
                Log.e(TAG, "Exception during sign in: $throwable")
            }
        }
    }

    private suspend fun signUp(
        context: Context,
        onError: () -> Unit = {},
        onSuccess: (GetCredentialResponse) -> Unit = {}
    ) {
        val credentialManager = CredentialManager.Companion.create(context)

        try {

            val result = credentialManager.getCredential(
                request = generateCredentialRequest(filterByAuthorizedAccounts = true),
                context = context
            )

            onSuccess(result)

        } catch (throwable: Throwable) {
            onError()
            Log.e(TAG, "Exception during sign in: $throwable")
        }
    }

    private fun generateCredentialRequest(filterByAuthorizedAccounts: Boolean): GetCredentialRequest {
        val googleIdOptionFalse: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
             .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(generateSecureRandomNonce())
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptionFalse)
            .build()
    }

    private fun checkGooglePlayServices(context: Context, onError: () -> Unit): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return if (resultCode == ConnectionResult.SUCCESS) {
            true
        } else {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(context as Activity, resultCode, 2404)?.show()
            } else {
                onError()
            }
            false
        }
    }

    private fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom.getInstanceStrong().nextBytes(randomBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
    }

    companion object {
        private const val TAG = "GoogleAuthManager"
    }
}