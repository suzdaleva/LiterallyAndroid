package com.silentspring.feature.login.presentation.screens.credentials

import android.content.Context
import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.silentspring.core.common.exceptions.NetworkException
import com.silentspring.core.common.utils.RegexPatterns.EMAIL_PATTERN
import com.silentspring.core.common.utils.RegexPatterns.PASSWORD_PATTERN
import com.silentspring.data.authorization.data.GoogleAuthManager
import com.silentspring.data.authorization.domain.usecase.LoginUseCase
import com.silentspring.data.authorization.domain.usecase.RegistrationUseCase
import com.silentspring.data.authorization.domain.usecase.VerifyGoogleIdTokenUseCase
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.screens.credentials.state.AuthorizationType
import com.silentspring.feature.login.presentation.screens.credentials.state.CredentialsState
import com.silentspring.feature.login.presentation.screens.credentials.state.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val verifyGoogleIdTokenUseCase: VerifyGoogleIdTokenUseCase,
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase,
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val mutableState = MutableStateFlow(CredentialsState())
    val state: StateFlow<CredentialsState> = mutableState

    private val mutableSideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect: SharedFlow<SideEffect> = mutableSideEffect

    fun onStateChanged(state: CredentialsState) {
        mutableState.update { state }
    }

    fun authenticate() = viewModelScope.launch {
        val currentState = mutableState.value
        if (mutableState.value.type == AuthorizationType.SIGN_UP) {
            register(
                currentState.username,
                currentState.email,
                currentState.password
            )
        } else {
            login(
                currentState.email,
                currentState.password
            )
        }
    }

    fun signInWithGoogle(context: Context) = viewModelScope.launch {
        googleAuthManager.signIn(
            context = context,
            onSuccess = ::handleGoogleSignIn,
            onError = { emitSideEffect(SideEffect.CommonError) },
            onPlayServicesError = { emitSideEffect(SideEffect.ShowMessage(messageRes = R.string.google_play_services_snackbar_message)) },
        )
    }

    fun handleGoogleSignIn(result: GetCredentialResponse) = viewModelScope.launch {

        val credential = result.credential

        if (credential is CustomCredential) {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    verifyGoogleIdToken(googleIdTokenCredential)
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(TAG, "Received an invalid google id token response $e")
                    mutableSideEffect.emit(SideEffect.CommonError)
                }
            } else {
                Log.e(TAG, "Unexpected type of credential")
                mutableSideEffect.emit(SideEffect.CommonError)
            }
        }
    }

    private suspend fun register(username: String, email: String, password: String) {
        try {
            if (!validateFields(email, password)) return
            mutableState.update { it.copy(isLoading = true) }
            registrationUseCase(
                username = username,
                email = email,
                password = password
            )
            mutableState.update { it.copy(isLoading = false, isSuccess = true) }
            mutableSideEffect.emit(SideEffect.RegistrationSuccess(email))
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }
            parseErrors(throwable)

        }
    }

    private suspend fun login(email: String, password: String) {
        try {

            if (!email.matches(EMAIL_PATTERN.toRegex())) {
                mutableState.update {
                    it.copy(
                        emailErrorRes = R.string.invalid_email_format_error
                    )
                }
                return
            }

            mutableState.update { it.copy(isLoading = true) }
            loginUseCase(
                email = email,
                password = password
            )
            mutableState.update { it.copy(isLoading = false, isSuccess = true) }
            mutableSideEffect.emit(SideEffect.LoginSuccess)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            mutableState.update { it.copy(isLoading = false) }
            (throwable as? NetworkException)?.let {
                when (it.status) {
                    401 -> mutableSideEffect.emit(SideEffect.ShowMessage(messageRes = R.string.invalid_credentials_snackbar_message))
                    403 -> mutableSideEffect.emit(SideEffect.NotVerifiedError(email))
                    409 -> mutableState.update { it.copy(emailErrorRes = R.string.email_registered_with_google_error) }
                    else -> mutableSideEffect.emit(SideEffect.CommonError)
                }
            } ?: mutableSideEffect.emit(SideEffect.CommonError)
        }
    }

    private fun validateFields(email: String, password: String): Boolean {
        val isEmailValid = email.matches(EMAIL_PATTERN.toRegex())
        val isPasswordValid = password.matches(PASSWORD_PATTERN.toRegex())

        mutableState.update {
            it.copy(
                emailErrorRes = R.string.invalid_email_format_error.takeIf { !isEmailValid },
                passwordErrorRes = R.string.invalid_password_format_error.takeIf { !isPasswordValid }
            )
        }
        return isEmailValid && isPasswordValid
    }

    private suspend fun parseErrors(throwable: Throwable) {
        if (throwable is NetworkException && throwable.status != 500) {
            when (throwable.status) {
                400 -> throwable.errors.forEach { error ->
                    mutableState.update {
                        it.copy(
                            usernameErrorRes = R.string.username_already_taken_error.takeIf {
                                error.field == USERNAME_FIELD && error.reasonCode == 409
                            },
                            emailErrorRes = R.string.invalid_email_format_error.takeIf {
                                error.field == EMAIL_FIELD && error.reasonCode == 400
                            } ?: R.string.email_already_registered_error.takeIf {
                                error.field == EMAIL_FIELD && error.reasonCode == 409
                            },
                            passwordErrorRes = R.string.invalid_password_format_error.takeIf {
                                error.field == PASSWORD_FIELD
                            }
                        )
                    }
                }

                409 -> mutableState.update {
                    it.copy(
                        emailErrorRes = R.string.email_registered_with_google_error
                    )
                }

                else -> Unit
            }
        } else {
            mutableSideEffect.emit(
                SideEffect.CommonError
            )
        }
    }

    private suspend fun verifyGoogleIdToken(googleIdTokenCredential: GoogleIdTokenCredential) {
        try {
            verifyGoogleIdTokenUseCase(googleIdTokenCredential.idToken)
            mutableSideEffect.emit(SideEffect.LoginSuccess)
        } catch (throwable: Throwable) {
            if (throwable is NetworkException && throwable.status == 409) {
                mutableSideEffect.emit(SideEffect.ShowMessage(messageRes = R.string.email_already_registered_with_google_snackbar_message))
            }
        }
    }

    private fun emitSideEffect(sideEffect: SideEffect) = viewModelScope.launch(Dispatchers.Main) {
        mutableSideEffect.emit(sideEffect)
    }

    companion object {
        private const val TAG = "CredentialsViewModel"
        private const val USERNAME_FIELD = "username"
        private const val EMAIL_FIELD = "email"
        private const val PASSWORD_FIELD = "password"
    }
}