package com.silentspring.literally.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.silentspring.feature.books.navigation.BooksList
import com.silentspring.feature.books.screens.books_list.BooksListScreen
import com.silentspring.feature.login.presentation.navigation.Authorization
import com.silentspring.feature.login.presentation.navigation.Credentials
import com.silentspring.feature.login.presentation.navigation.PrivacyPolicy
import com.silentspring.feature.login.presentation.navigation.ResetPassword
import com.silentspring.feature.login.presentation.navigation.Verification
import com.silentspring.feature.login.presentation.screens.credentials.CredentialsScreen
import com.silentspring.feature.login.presentation.screens.privacy_policy.PrivacyPolicyScreen
import com.silentspring.feature.login.presentation.screens.reset_password.ResetPasswordScreen
import com.silentspring.feature.login.presentation.screens.verification.VerificationScreen
import com.silentspring.feature.profile.navigation.Profile
import com.silentspring.feature.profile.screens.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object Root


@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Root
    ) {
        composable<Root> {
            Box(modifier = Modifier.fillMaxSize()) {}
        }
        composable<Profile> {
            ProfileScreen()
        }
        composable<BooksList> {
            BooksListScreen()
        }
        navigation<Authorization>(startDestination = Credentials) {
            composable<Credentials> {
                CredentialsScreen(
                    onPrivacyPolicyClick = { navController.navigate(PrivacyPolicy) },
                    onRegistrationSuccess = { email -> navController.navigate(Verification(email)) },
                    onNotVerifiedError = { email -> navController.navigate(Verification(email)) },
                    onLoginSuccess = navController::navigateToAuthArea,
                    onForgotPasswordClick = { navController.navigate(ResetPassword) }
                )
            }
            composable<Verification> {
                VerificationScreen(
                    onVerificationSuccess = navController::navigateToAuthArea
                )
            }
            composable<PrivacyPolicy> {
                PrivacyPolicyScreen(
                    onBackClick = navController::popBackStack
                )
            }
            composable<ResetPassword> {
                ResetPasswordScreen(
                    onResetPasswordSuccess = navController::navigateToAuthArea,
                    onBackClick = navController::popBackStack
                )
            }
        }
    }
}