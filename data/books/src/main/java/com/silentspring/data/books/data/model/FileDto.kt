package com.silentspring.data.books.data.model

internal data class FileDto(
    val title: String? = null,
    val author: String? = null,
    val path: String? = null,
    val genre: String? = null,
    val child: String? = null,
    val annotation: String? = null,
    val fileExtension: FileExtension? = null,
    val size: Long = 0,
    val pages: Int = 0,
    val language: String? = null,
    val parentPath: String? = null,
    val pagesRead: Int = 0
)