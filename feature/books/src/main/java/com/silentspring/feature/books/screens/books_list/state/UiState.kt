package com.silentspring.feature.books.screens.books_list.state

import com.silentspring.feature.books.items.FolderItem

data class BooksState(
    val items: List<FolderItem> = emptyList(),
    val searchQuery: String = ""
)