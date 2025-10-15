package com.silentspring.uikit.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue

private val DarkColorScheme = darkColorScheme(
    primary = primaryBlack,
    secondary = primaryWhite
)

@Composable
fun LiterallyTheme(
    vararg values: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(values = values) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = LiterallyTypography,
            content = content
        )
    }
}