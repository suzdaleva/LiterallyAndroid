package com.silentspring.feature.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.silentspring.uikit.components.LiterallyTypography

@Composable
fun ProfileScreen() {

    val viewModel = hiltViewModel<ProfileViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile", style = LiterallyTypography.headlineLarge)
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = state.name,
            style = LiterallyTypography.bodySmall
        )
    }
}