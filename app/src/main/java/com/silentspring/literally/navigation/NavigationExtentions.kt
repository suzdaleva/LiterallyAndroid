package com.silentspring.literally.navigation

import androidx.navigation.NavHostController
import com.silentspring.feature.books.navigation.BooksList
import com.silentspring.feature.login.presentation.navigation.Authorization

fun NavHostController.navigateToRoute(route: Any, popUpToRoute: Any) {
    navigate(
        route, builder = {
            popUpTo(popUpToRoute) {
                this.inclusive = true
            }
        }
    )
}

fun NavHostController.navigateToAuthArea() {
    navigateToRoute(route = BooksList, popUpToRoute = Authorization)
}