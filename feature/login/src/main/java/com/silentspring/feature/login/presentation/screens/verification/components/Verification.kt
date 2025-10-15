package com.silentspring.feature.login.presentation.screens.verification.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.feature.login.R
import com.silentspring.feature.login.presentation.common.CountdownFooter
import com.silentspring.feature.login.presentation.screens.verification.state.VerificationState
import com.silentspring.uikit.components.LiterallyTypography
import com.silentspring.uikit.components.OutlinedTextField


@Composable
internal fun Verification(
    state: VerificationState,
    onStateChanged: (VerificationState) -> Unit,
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
            text = stringResource(R.string.verification_title),
            style = LiterallyTypography.headlineLarge
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.verification_description, state.email),
            style = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp),
            text = state.code,
            supportingText = state.codeSupportingTestRes?.let { stringResource(it) },
            isError = state.isCodeError,
            labelText = stringResource(R.string.verification_code_hint),
            onValueChange = {
                onStateChanged(
                    state.copy(
                        code = it,
                        codeSupportingTestRes = null,
                        isCodeError = false
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        CountdownFooter(
            timerText = state.timerText,
            onResendClick = onResendClick,
            isContinueEnabled = state.code.isNotEmpty() && !state.isCodeError,
            onContinueClick = onContinueClick
        )
    }
}

@Preview
@Composable
private fun VerificationPreview() {
    Verification(
        state = VerificationState(
            code = "",
            email = "test@test.com"
        ),
        onStateChanged = {},
        onContinueClick = {},
        onResendClick = {}
    )
}