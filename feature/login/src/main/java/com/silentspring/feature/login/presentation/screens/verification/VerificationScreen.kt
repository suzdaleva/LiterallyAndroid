package com.silentspring.feature.login.presentation.screens.verification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silentspring.feature.login.presentation.screens.verification.components.Verification
import com.silentspring.feature.login.presentation.screens.verification.state.SideEffect
import com.silentspring.uikit.components.Loader
import com.silentspring.uikit.components.LocalSnackbarController
import com.silentspring.uikit.components.SnackbarEvent
import kotlinx.coroutines.flow.collectLatest


@Composable
fun VerificationScreen(
    onVerificationSuccess: () -> Unit
) {

    val viewModel = hiltViewModel<VerificationViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarController = LocalSnackbarController.current

    Verification(
        state = state,
        onStateChanged = viewModel::onStateChanged,
        onContinueClick = viewModel::verify,
        onResendClick = viewModel::resendCode
    )

    if (state.isLoading) {
        Loader()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                SideEffect.CommonError -> snackbarController.sendCommonError()
                SideEffect.VerificationSuccess -> onVerificationSuccess()
                is SideEffect.ShowMessage -> snackbarController.sendEvent(
                    SnackbarEvent(
                        messageRes = sideEffect.messageRes
                    )
                )
            }
        }
    }
}