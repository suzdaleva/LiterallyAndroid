package com.silentspring.feature.login.presentation.screens.credentials.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.screens.credentials.state.AuthorizationType
import com.silentspring.feature.login.presentation.screens.credentials.state.CredentialsState
import com.silentspring.uikit.R as UiKit
import com.silentspring.uikit.components.GlowButton
import com.silentspring.uikit.components.GlowSwitch
import com.silentspring.uikit.components.LiterallyTypography
import com.silentspring.uikit.components.OutlinedButton
import com.silentspring.uikit.components.OutlinedTextField
import com.silentspring.uikit.components.PasswordTextField
import com.silentspring.uikit.utils.drawCustomIndicatorLine

@Composable
internal fun Credentials(
    state: CredentialsState,
    onStateChanged: (CredentialsState) -> Unit,
    onButtonClick: () -> Unit,
    onGoogleButtonClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 60.dp)) {
            Text(
                modifier = Modifier.padding(top = 20.dp, bottom = 34.dp),
                text = stringResource(state.type.titleRes),
                style = LiterallyTypography.headlineLarge
            )

            AnimatedVisibility(
                visible = state.type == AuthorizationType.SIGN_UP,
                enter = expandVertically(tween(200)) + fadeIn(tween(200)),
                exit = shrinkVertically(tween(200)) + fadeOut(tween(200))
            ) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = state.username,
                        supportingText = state.usernameErrorRes?.let { stringResource(it) },
                        isError = state.usernameErrorRes != null,
                        labelText = stringResource(R.string.username_hint),
                        onValueChange = {
                            if (it.length <= 25) onStateChanged(
                                state.copy(
                                    username = it,
                                    usernameErrorRes = null
                                )
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                text = state.email,
                supportingText = state.emailErrorRes?.let { stringResource(it) },
                isError = state.emailErrorRes != null,
                labelText = stringResource(R.string.email_hint),
                onValueChange = {
                    onStateChanged(
                        state.copy(
                            email = it,
                            emailErrorRes = null
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            PasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 32.dp),
                text = state.password,
                supportingText = state.passwordErrorRes?.let { stringResource(it) },
                isError = state.passwordErrorRes != null,
                labelText = stringResource(R.string.password_hint),
                onValueChange = {
                    if (it.length <= 25) onStateChanged(
                        state.copy(
                            password = it,
                            passwordErrorRes = null
                        )
                    )
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                if (state.type == AuthorizationType.SIGN_UP) {
                    TermsAndPolicyText(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterStart)
                            .padding(end = 56.dp),
                        onPolicyClick = onPrivacyPolicyClick
                    )
                    GlowSwitch(
                        modifier = Modifier.align(alignment = Alignment.CenterEnd),
                        checked = state.isPolicyChecked,
                        onCheckedChange = { onStateChanged(state.copy(isPolicyChecked = it)) }
                    )
                } else {
                    Text(
                        text = buildAnnotatedString {
                            withLink(
                                LinkAnnotation.Clickable(tag = "Forget password") {
                                    onForgotPasswordClick()
                                }) {
                                append(stringResource(R.string.forgot_password))
                            }
                        },
                        style = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp)
                    )
                }
            }

            GlowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(state.type.buttonTextRes),
                enabled = state.isAuthEnabled(),
                onClick = onButtonClick
            )

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                enabled = !state.isLoading,
                leadingIcon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(UiKit.drawable.icon_google),
                        tint = Color.Unspecified,
                        contentDescription = null
                    )
                },
                onClick = onGoogleButtonClick,
                text = stringResource(state.type.googleButtonTextRes)
            )
        }

        FlowRow(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(
                text = "${stringResource(state.type.footerTextRes)} ",
                style = LiterallyTypography.bodyLarge.copy(fontSize = 16.sp)
            )
            Text(
                modifier = Modifier
                    .drawCustomIndicatorLine()
                    .clickable {
                        onStateChanged(CredentialsState(type = state.type.switchType()))
                    },
                text = stringResource(state.type.footerActionTextRes),
                style = LiterallyTypography.bodyLarge.copy(fontSize = 16.sp)
            )
        }
    }
}

@Composable
private fun TermsAndPolicyText(
    modifier: Modifier = Modifier,
    onPolicyClick: () -> Unit,
    style: TextStyle = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp)
) {
    FlowRow(modifier = modifier) {
        Text(
            text = "${stringResource(R.string.agreement_body_first)} ",
            style = style
        )
        Text(
            modifier = Modifier
                .drawCustomIndicatorLine()
                .clickable(onClick = onPolicyClick),
            text = stringResource(R.string.privacy_policy),
            style = style
        )
    }
}