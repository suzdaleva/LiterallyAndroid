package com.silentspring.feature.login.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString.Builder
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silentspring.feature.login.R
import com.silentspring.uikit.components.GlowButton
import com.silentspring.uikit.components.LiterallyTypography
import com.silentspring.uikit.components.primaryGrey

@Composable
internal fun CountdownFooter(
    timerText: String?,
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onResendClick: () -> Unit
) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            text = buildAnnotatedString {
                append("${stringResource(R.string.did_not_receive_code)} ")
                if (timerText == null) {
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "Resend code"
                        ) {
                            onResendClick()
                        }) {
                        withFooterStyle(stringResource(R.string.resend))
                    }
                } else {
                    withFooterStyle(stringResource(R.string.resend_timer, timerText))
                }
            },
            style = LiterallyTypography.bodyLarge.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center
        )
        GlowButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.continue_button_text),
            enabled = isContinueEnabled,
            onClick = onContinueClick
        )
    }
}

private fun Builder.withFooterStyle(text: String) {
    withStyle(
        style =
            LiterallyTypography.bodyLarge.copy(
                fontSize = 14.sp,
                color = primaryGrey
            ).toSpanStyle()
    ) {
        append(text)
    }
}