package com.silentspring.feature.login.presentation.screens.privacy_policy

import androidx.compose.runtime.Composable
import com.silentspring.uikit.components.WebView


internal const val PRIVACY_POLICY_URL =
    "https://www.freeprivacypolicy.com/live/a0da735c-74e2-4268-86cb-fa4202962151"

@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    WebView(
        url = PRIVACY_POLICY_URL,
        onBackClick = onBackClick
    )
}