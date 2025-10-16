package com.silentspring.feature.books.items

import com.silentspring.data.books.domain.model.FolderBusiness

data class FolderItem(
    val title: String,
    val filesCount: Int
)

fun FolderBusiness.toItem() = FolderItem(title = title, filesCount = files.size)
