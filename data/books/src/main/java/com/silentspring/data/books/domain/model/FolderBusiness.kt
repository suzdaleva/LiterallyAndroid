package com.silentspring.data.books.domain.model

data class FolderBusiness(
    val title: String = "",
    val files: List<FileBusiness> = emptyList(),
    val folders: List<FolderBusiness> = emptyList()
)
