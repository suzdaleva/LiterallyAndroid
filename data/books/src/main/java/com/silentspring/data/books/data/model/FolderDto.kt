package com.silentspring.data.books.data.model

import com.silentspring.data.books.domain.model.FolderBusiness

internal data class FolderDto(
    val title: String = "",
    val filesCount: Int = 0
)

internal fun FolderDto.toBusiness() = FolderBusiness(title = title, filesCount = filesCount)