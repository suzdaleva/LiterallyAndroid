package com.silentspring.feature.login.presentation.screens.reset_password.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.common.CountdownFooter
import com.silentspring.feature.login.presentation.screens.reset_password.state.ResetPasswordState
import com.silentspring.feature.login.presentation.screens.reset_password.state.ResetPasswordStep
import com.silentspring.uikit.components.GlowButton
import com.silentspring.uikit.components.LiterallyTypography
import com.silentspring.uikit.components.OutlinedTextField
import com.silentspring.uikit.components.PasswordTextField
import com.silentspring.uikit.utils.drawCustomIndicatorLine
import kotlin.text.isNotEmpty


@Composable
internal fun ResetPassword(
    state: ResetPasswordState,
    onStateChanged: (ResetPasswordState) -> Unit,
    onResetPasswordClick: () -> Unit,
    onSubmitClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onResendClick: () -> Unit,
    onContinueClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(state.step.titleRes),
            style = LiterallyTypography.headlineLarge
        )

        when (state.step) {
            ResetPasswordStep.EMAIL -> {
                EmailStep(
                    email = state.email,
                    emailErrorRes = state.emailErrorRes,
                    onEmailChange = {
                        onStateChanged(
                            state.copy(
                                email = it,
                                emailErrorRes = null
                            )
                        )
                    },
                    onNavigateToLogin = onNavigateToLogin,
                    onSubmitClick = onSubmitClick
                )
            }

            ResetPasswordStep.CODE -> {
                CodeStep(
                    email = state.email,
                    timerText = state.timerText,
                    resetPasswordCode = state.resetPasswordCode,
                    resetPasswordCodeErrorRes = state.resetPasswordCodeErrorRes,
                    onCodeChange = {
                        onStateChanged(
                            state.copy(
                                resetPasswordCode = it,
                                resetPasswordCodeErrorRes = null
                            )
                        )
                    },
                    onResendClick = onResendClick,
                    onContinueClick = onContinueClick
                )
            }

            ResetPasswordStep.NEW_PASSWORD -> {
                NewPasswordStep(
                    newPassword = state.newPassword,
                    newPasswordErrorRes = state.newPasswordErrorRes,
                    confirmPassword = state.confirmPassword,
                    confirmPasswordErrorRes = state.confirmPasswordErrorRes,
                    onNewPasswordChange = {
                        onStateChanged(
                            state.copy(
                                newPassword = it,
                                newPasswordErrorRes = null
                            )
                        )
                    },
                    onConfirmPasswordChange = {
                        onStateChanged(
                            state.copy(
                                confirmPassword = it,
                                confirmPasswordErrorRes = null
                            )
                        )
                    },
                    onResetPasswordClick = onResetPasswordClick,
                    onNavigateToLogin = onNavigateToLogin
                )
            }
        }
    }

}


@Composable
private fun ColumnScope.EmailStep(
    email: String,
    emailErrorRes: Int? = null,
    onEmailChange: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onSubmitClick: () -> Unit
) {

    Text(
        modifier = Modifier.padding(top = 32.dp),
        text = stringResource(R.string.forgot_password_description),
        style = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp)
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
        text = email,
        supportingText = emailErrorRes?.let { stringResource(it) },
        isError = emailErrorRes != null,
        labelText = stringResource(R.string.email_hint),
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )

    GlowButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = stringResource(R.string.submit_button_text),
        enabled = email.isNotBlank() && emailErrorRes == null,
        onClick = onSubmitClick
    )

    Spacer(modifier = Modifier.weight(1f))

    Text(
        modifier = Modifier
            .drawCustomIndicatorLine()
            .clickable(onClick = onNavigateToLogin),
        text = stringResource(R.string.navigate_back_to_login),
        style = LiterallyTypography.bodyLarge.copy(fontSize = 16.sp)
    )
}


@Composable
private fun ColumnScope.CodeStep(
    email: String,
    timerText: String?,
    resetPasswordCode: String,
    resetPasswordCodeErrorRes: Int? = null,
    onCodeChange: (String) -> Unit,
    onResendClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    Text(
        modifier = Modifier.padding(top = 32.dp),
        text = AnnotatedString.fromHtml(stringResource(R.string.check_email_description, email)),
        style = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp)
    )

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
        text = resetPasswordCode,
        supportingText = resetPasswordCodeErrorRes?.let { stringResource(it) },
        isError = resetPasswordCodeErrorRes != null,
        labelText = stringResource(R.string.reset_password_code_hint),
        onValueChange = onCodeChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )

    Spacer(modifier = Modifier.weight(1f))

    CountdownFooter(
        timerText = timerText,
        onResendClick = onResendClick,
        isContinueEnabled = resetPasswordCode.isNotEmpty() && resetPasswordCodeErrorRes == null,
        onContinueClick = onContinueClick
    )
}


@Composable
private fun ColumnScope.NewPasswordStep(
    newPassword: String,
    newPasswordErrorRes: Int? = null,
    confirmPassword: String,
    confirmPasswordErrorRes: Int? = null,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onResetPasswordClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    PasswordTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = newPassword,
        supportingText = newPasswordErrorRes?.let { stringResource(it) },
        isError = newPasswordErrorRes != null,
        labelText = stringResource(R.string.new_password_hint),
        onValueChange = onNewPasswordChange
    )

    PasswordTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        text = confirmPassword,
        supportingText = confirmPasswordErrorRes?.let { stringResource(it) },
        isError = confirmPasswordErrorRes != null,
        labelText = stringResource(R.string.repeat_new_password_hint),
        onValueChange = onConfirmPasswordChange
    )

    GlowButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        text = stringResource(R.string.reset_password_button_text),
        enabled = newPassword.isNotBlank() && newPasswordErrorRes == null && confirmPassword.isNotBlank() && confirmPasswordErrorRes == null,
        onClick = onResetPasswordClick
    )

    Spacer(modifier = Modifier.weight(1f))

    Text(
        modifier = Modifier
            .drawCustomIndicatorLine()
            .clickable(onClick = onNavigateToLogin),
        text = stringResource(R.string.navigate_back_to_login),
        style = LiterallyTypography.bodyLarge.copy(fontSize = 16.sp)
    )

}