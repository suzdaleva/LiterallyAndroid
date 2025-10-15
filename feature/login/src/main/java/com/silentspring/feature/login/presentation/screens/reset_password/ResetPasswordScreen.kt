package com.silentspring.feature.login.presentation.screens.reset_password

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silentspring.feature.login.presentation.screens.reset_password.components.ResetPassword
import com.silentspring.feature.login.presentation.screens.reset_password.state.ResetPasswordStep
import com.silentspring.feature.login.presentation.screens.reset_password.state.SideEffect
import com.silentspring.uikit.components.Loader
import com.silentspring.uikit.components.LocalSnackbarController
import com.silentspring.uikit.components.SnackbarEvent
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ResetPasswordScreen(
    onResetPasswordSuccess: () -> Unit,
    onBackClick: () -> Unit
) {

    val viewModel = hiltViewModel<ResetPasswordViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarController = LocalSnackbarController.current

    BackHandler(
        onBack = onBackClick.takeIf { state.step == ResetPasswordStep.EMAIL }
            ?: viewModel::changeStep
    )

    ResetPassword(
        state = state,
        onStateChanged = viewModel::onStateChanged,
        onNavigateToLogin = onBackClick,
        onResetPasswordClick = viewModel::resetPassword,
        onResendClick = viewModel::resendCode,
        onContinueClick = viewModel::verifyCode,
        onSubmitClick = viewModel::forgotPassword
    )

    if (state.isLoading) {
        Loader()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                SideEffect.CommonError -> snackbarController.sendCommonError()
                SideEffect.ResetPasswordSuccess -> onResetPasswordSuccess()
                is SideEffect.ShowMessage -> snackbarController.sendEvent(
                    SnackbarEvent(
                        messageRes = sideEffect.massageRes
                    )
                )
            }
        }
    }
}