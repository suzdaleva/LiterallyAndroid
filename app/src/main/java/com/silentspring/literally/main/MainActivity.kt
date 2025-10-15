package com.silentspring.literally.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.silentspring.feature.books.navigation.BooksList
import com.silentspring.feature.login.presentation.navigation.Authorization
import com.silentspring.literally.main.state.SideEffect
import com.silentspring.uikit.components.LiterallyTheme
import com.silentspring.literally.navigation.MainNavHost
import com.silentspring.literally.navigation.Root
import com.silentspring.literally.navigation.navigateToRoute
import com.silentspring.uikit.components.LocalSnackbarController
import com.silentspring.uikit.components.ObserveAsEvents
import com.silentspring.uikit.components.SnackbarController
import com.silentspring.uikit.components.primaryBlack
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )

        setContent {

            val navController = rememberNavController()
            val snackbarHostState = remember { SnackbarHostState() }
            val scope = rememberCoroutineScope()

            ObserveAsEvents(
                flow = SnackbarController.events,
                snackbarHostState
            ) { event ->

                scope.launch {
                    snackbarHostState.currentSnackbarData?.dismiss()

                    val result = snackbarHostState.showSnackbar(
                        message = this@MainActivity.getString(event.messageRes),
                        actionLabel = event.action?.nameRes?.let { this@MainActivity.getString(it) },
                        duration = SnackbarDuration.Short
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        event.action?.action?.invoke()
                    }
                }
            }

            LaunchedEffect(Unit) {
                viewModel.sideEffect.collectLatest { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.NavigateToRoute -> {
                            navController.navigateToRoute(route = BooksList.takeIf { sideEffect.isAuthorized }
                                ?: Authorization, popUpToRoute = Root)
                        }
                    }
                }
            }

            LiterallyTheme(
                LocalSnackbarController provides SnackbarController
            ) {
                Scaffold(
                    snackbarHost = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 20.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    containerColor = primaryBlack
                ) { innerPadding ->
                    MainNavHost(
                        modifier = Modifier
                            .padding(innerPadding),
                        navController = navController
                    )
                }
            }
        }
    }
}