package com.silentspring.feature.login.presentation.screens.reset_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silentspring.core.common.exceptions.NetworkException
import com.silentspring.core.common.utils.RegexPatterns.EMAIL_PATTERN
import com.silentspring.core.common.utils.RegexPatterns.PASSWORD_PATTERN
import com.silentspring.core.common.utils.SimpleTimer
import com.silentspring.data.authorization.domain.usecase.ForgotPasswordUseCase
import com.silentspring.data.authorization.domain.usecase.ResetPasswordCodeVerificationUseCase
import com.silentspring.data.authorization.domain.usecase.ResetPasswordUseCase
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.screens.reset_password.state.ResetPasswordState
import com.silentspring.feature.login.presentation.screens.reset_password.state.ResetPasswordStep
import com.silentspring.feature.login.presentation.screens.reset_password.state.SideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
internal class ResetPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val resetPasswordCodeVerificationUseCase: ResetPasswordCodeVerificationUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(ResetPasswordState())
    val state: StateFlow<ResetPasswordState> = mutableState

    private val mutableSideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect: SharedFlow<SideEffect> = mutableSideEffect

    private var timer = SimpleTimer()

    fun onStateChanged(state: ResetPasswordState) {
        mutableState.update { state }
    }

    fun changeStep() {
        mutableState.update { state ->
            when (state.step) {
                ResetPasswordStep.EMAIL -> state
                ResetPasswordStep.CODE -> state.copy(
                    step = ResetPasswordStep.EMAIL,
                    resetPasswordCode = "",
                    resetPasswordCodeErrorRes = null,
                    timerText = null
                )

                ResetPasswordStep.NEW_PASSWORD -> state.copy(
                    step = ResetPasswordStep.CODE,
                    newPassword = "",
                    newPasswordErrorRes = null,
                    confirmPassword = "",
                    confirmPasswordErrorRes = null
                )
            }
        }
    }

    fun forgotPassword() = viewModelScope.launch {
        try {
            val currentState = mutableState.value

            if (!currentState.email.matches(EMAIL_PATTERN.toRegex())) {
                mutableState.update {
                    it.copy(
                        emailErrorRes = R.string.invalid_email_format_error
                    )
                }
                return@launch
            }

            mutableState.update { it.copy(isLoading = true) }

            forgotPasswordUseCase(email = currentState.email)

            mutableState.update {
                it.copy(
                    isLoading = false,
                    step = ResetPasswordStep.CODE
                )
            }
            startTimer()
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }
            (throwable as? NetworkException)?.let {
                when (it.status) {
                    404 -> mutableSideEffect.emit(SideEffect.ShowMessage(R.string.user_not_found_error))
                    409 -> mutableState.update { it.copy(emailErrorRes = R.string.email_registered_with_google_error) }
                    else -> mutableSideEffect.emit(SideEffect.CommonError)
                }

            } ?: mutableSideEffect.emit(SideEffect.CommonError)
        }
    }

    fun verifyCode() = viewModelScope.launch {
        try {
            val currentState = mutableState.value
            mutableState.update { it.copy(isLoading = true) }

            resetPasswordCodeVerificationUseCase(
                email = currentState.email,
                resetPasswordCode = currentState.resetPasswordCode.toInt()
            )

            mutableState.update {
                it.copy(
                    isLoading = false,
                    step = ResetPasswordStep.NEW_PASSWORD
                )
            }
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }
            (throwable as? NetworkException)?.let {
                if (throwable.status == 404) {
                    mutableSideEffect.emit(SideEffect.ShowMessage(R.string.user_not_found_error))
                }
                mutableState.update {
                    it.copy(
                        resetPasswordCodeErrorRes = when (throwable.status) {
                            400 -> R.string.invalid_code_error
                            403 -> R.string.code_expired_error
                            else -> null
                        }
                    )
                }
            } ?: mutableSideEffect.emit(SideEffect.CommonError)
        }
    }

    fun resendCode() = viewModelScope.launch {
        try {
            val currentState = mutableState.value
            mutableState.update { it.copy(isLoading = true) }
            forgotPasswordUseCase(email = currentState.email)
            mutableState.update { it.copy(isLoading = false) }
            startTimer()
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }
            val sideEffect = (throwable as? NetworkException)?.let {
                SideEffect.ShowMessage(R.string.user_not_found_error)
                    .takeIf { throwable.status == 404 }
            } ?: SideEffect.CommonError
            mutableSideEffect.emit(sideEffect)
        }
    }

    fun resetPassword() = viewModelScope.launch {
        try {
            val currentState = mutableState.value
            if (!validateFields(
                    currentState.newPassword,
                    currentState.confirmPassword
                )
            ) return@launch

            mutableState.update { it.copy(isLoading = true) }
            resetPasswordUseCase(
                email = currentState.email,
                newPassword = currentState.newPassword
            )
            mutableState.update { it.copy(isLoading = false) }
            mutableSideEffect.emit(SideEffect.ResetPasswordSuccess)
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }
            val sideEffect =
                SideEffect.ShowMessage(R.string.user_not_found_error)
                    .takeIf { (throwable as? NetworkException)?.status == 404 }
                    ?: SideEffect.CommonError
            mutableSideEffect.emit(sideEffect)
        }
    }

    private fun validateFields(newPassword: String, confirmPassword: String): Boolean {
        val isNewPasswordValid = newPassword.matches(PASSWORD_PATTERN.toRegex())
        val isConfirmPasswordValid = newPassword == confirmPassword

        mutableState.update {
            it.copy(
                newPasswordErrorRes = R.string.invalid_password_format_error.takeIf { !isNewPasswordValid },
                confirmPasswordErrorRes = R.string.passwords_do_not_match_error.takeIf { !isConfirmPasswordValid }
            )
        }

        return isNewPasswordValid && isConfirmPasswordValid
    }

    private fun startTimer() {
        timer.start(
            scope = viewModelScope,
            onTick = { timerText ->
                mutableState.update { it.copy(timerText = timerText) }
            },
            onStop = {
                mutableState.update { it.copy(timerText = null) }
            }
        )
    }
}