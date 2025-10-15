package com.silentspring.feature.books.screens.books_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silentspring.data.books.domain.usecase.GetFoldersUseCase
import com.silentspring.feature.books.items.toItem
import com.silentspring.feature.books.screens.books_list.state.BooksState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val getFoldersUseCase: GetFoldersUseCase
) : ViewModel() {

    private val mutableState = MutableStateFlow(BooksState())
    val state: StateFlow<BooksState> = mutableState

    fun getFolders() = viewModelScope.launch {
        val folders = getFoldersUseCase().map { it.toItem() }
        mutableState.update { it.copy(folders) }
    }

    fun onStateChanged(state: BooksState) {
        mutableState.update { state }
    }
}