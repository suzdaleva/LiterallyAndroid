package com.silentspring.feature.books.screens.books_list.state

import android.os.Environment
import com.silentspring.feature.books.items.FolderItem

data class BooksState(
    val items: List<FolderItem> = emptyList(),
    val currentFolder: String = Environment.getExternalStorageDirectory().path,
    val searchQuery: String = "",
)