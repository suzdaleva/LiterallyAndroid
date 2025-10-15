package com.silentspring.literally.main.state

sealed interface SideEffect {
    data class NavigateToRoute(val isAuthorized: Boolean) : SideEffect
}