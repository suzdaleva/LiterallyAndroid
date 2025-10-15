package com.silentspring.uikit.components

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
    error("SnackbarController composition error")
}