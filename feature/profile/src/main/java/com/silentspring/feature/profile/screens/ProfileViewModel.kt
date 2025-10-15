package com.silentspring.feature.profile.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silentspring.data.profile.domain.usecase.GetProfileUseCase
import com.silentspring.feature.profile.screens.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = mutableState

    init {
        viewModelScope.launch {
            try {
                val name = getProfileUseCase().name
                mutableState.update { it.copy(name = name) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}