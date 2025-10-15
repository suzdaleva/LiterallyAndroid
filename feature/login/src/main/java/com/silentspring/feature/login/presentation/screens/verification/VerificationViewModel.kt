package com.silentspring.feature.login.presentation.screens.verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.silentspring.core.common.exceptions.NetworkException
import com.silentspring.core.common.utils.SimpleTimer
import com.silentspring.data.authorization.domain.usecase.ResendCodeUseCase
import com.silentspring.data.authorization.domain.usecase.VerificationUseCase
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.navigation.Verification
import com.silentspring.feature.login.presentation.screens.verification.state.SideEffect
import com.silentspring.feature.login.presentation.screens.verification.state.VerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class VerificationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verificationUseCase: VerificationUseCase,
    private val resendCodeUseCase: ResendCodeUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        VerificationState(
            email = savedStateHandle.toRoute<Verification>().email
        )
    )

    val state: StateFlow<VerificationState> = mutableState

    private val mutableSideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect: SharedFlow<SideEffect> = mutableSideEffect

    private var timer = SimpleTimer()

    init {
        startTimer()
    }

    fun onStateChanged(state: VerificationState) {
        mutableState.update { state }
    }

    fun resendCode() = viewModelScope.launch {
        try {
            val currentState = mutableState.value
            resendCodeUseCase.invoke(currentState.email)
            mutableSideEffect.emit(SideEffect.ShowMessage(R.string.new_code_sent_snackbar_message))
            startTimer()
        } catch (throwable: Throwable) {
            (throwable as? NetworkException)?.let { throwable ->

                val errorRes = R.string.user_not_found_error.takeIf {
                    throwable.status == 404
                } ?: R.string.user_already_verified_error.takeIf {
                    throwable.status == 409
                }

                errorRes?.let { mutableSideEffect.emit(SideEffect.ShowMessage(it)) }
            } ?: mutableSideEffect.emit(SideEffect.CommonError)
        }
    }

    fun verify() = viewModelScope.launch {
        try {
            val currentState = mutableState.value
            mutableState.update { it.copy(isLoading = true) }
            verificationUseCase.invoke(
                email = currentState.email,
                verificationCode = currentState.code.toInt()
            )
            mutableState.update { it.copy(isLoading = false) }
            mutableSideEffect.emit(SideEffect.VerificationSuccess)
        } catch (throwable: Throwable) {
            mutableState.update { it.copy(isLoading = false) }

            (throwable as? NetworkException)?.let {

                mutableState.update {
                    it.copy(
                        codeSupportingTestRes = when (throwable.status) {
                            400 -> R.string.invalid_code_error
                            403 -> R.string.code_expired_error
                            else -> null
                        },
                        isCodeError = true
                    )
                }

                val errorRes = R.string.user_not_found_error.takeIf {
                    throwable.status == 404
                } ?: R.string.user_already_verified_error.takeIf {
                    throwable.status == 409
                }

                errorRes?.let { mutableSideEffect.emit(SideEffect.ShowMessage(it)) }
            } ?: mutableSideEffect.emit(SideEffect.CommonError)
        }
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