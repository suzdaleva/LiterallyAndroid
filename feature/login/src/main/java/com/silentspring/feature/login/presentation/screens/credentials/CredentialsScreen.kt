package com.silentspring.feature.login.presentation.screens.credentials

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.screens.credentials.components.Credentials
import com.silentspring.feature.login.presentation.screens.credentials.state.SideEffect
import com.silentspring.uikit.components.Loader
import com.silentspring.uikit.components.LocalSnackbarController
import com.silentspring.uikit.components.SnackbarEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CredentialsScreen(
    onPrivacyPolicyClick: () -> Unit,
    onRegistrationSuccess: (String) -> Unit,
    onNotVerifiedError: (String) -> Unit,
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {

    val viewModel = hiltViewModel<CredentialsViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val snackbarController = LocalSnackbarController.current
    val context = LocalContext.current

    Credentials(
        state = state,
        onStateChanged = viewModel::onStateChanged,
        onButtonClick = viewModel::authenticate,
        onGoogleButtonClick = { viewModel.signInWithGoogle(context) },
        onForgotPasswordClick = onForgotPasswordClick,
        onPrivacyPolicyClick = onPrivacyPolicyClick
    )

    if (state.isLoading) {
        Loader()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                SideEffect.CommonError -> snackbarController.sendCommonError()
                SideEffect.LoginSuccess -> onLoginSuccess()
                is SideEffect.ShowMessage -> snackbarController.sendEvent(SnackbarEvent(messageRes = sideEffect.messageRes))
                is SideEffect.NotVerifiedError -> {
                    snackbarController.sendEvent(SnackbarEvent(messageRes = R.string.not_verified_error_snackbar_message))
                    onNotVerifiedError(sideEffect.email)
                }

                is SideEffect.RegistrationSuccess -> onRegistrationSuccess(sideEffect.email)
            }
        }
    }
}