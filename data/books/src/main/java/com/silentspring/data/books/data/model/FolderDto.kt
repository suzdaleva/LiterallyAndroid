package com.silentspring.data.books.data.model

import com.silentspring.data.books.domain.model.FolderBusiness

internal data class FolderDto(
    val title: String = "",
    val files: List<FileDto> = emptyList(),
    val folders: List<FolderDto> = emptyList()
)

internal fun FolderDto.toBusiness(): FolderBusiness {
    return FolderBusiness(
        title = title,
        files = files.map { it.toBusiness() },
        folders = folders.map { it.toBusiness() }
    )
}