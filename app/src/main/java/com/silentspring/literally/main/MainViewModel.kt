package com.silentspring.literally.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silentspring.literally.main.state.SideEffect
import com.silentspring.storage.impl.usecases.GetIsAuthorizedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(
    getIsAuthorizedUseCase: GetIsAuthorizedUseCase
) : ViewModel() {

    private val mutableSideEffect = MutableSharedFlow<SideEffect>()
    val sideEffect: SharedFlow<SideEffect> = mutableSideEffect

    init {
        getIsAuthorizedUseCase()
            .onEach { isAuthorized ->
                mutableSideEffect.emit(SideEffect.NavigateToRoute(isAuthorized))
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    }
}