package com.silentspring.data.books.data.model

import com.silentspring.data.books.domain.model.FileBusiness

internal data class FileDto(
    val title: String? = null,
    val author: String? = null,
    val path: String? = null,
    val genre: String? = null,
    val annotation: String? = null,
    val fileExtension: FileExtension? = null,
    val size: Long = 0,
    val pages: Int = 0,
    val language: String? = null,
    val parentPath: String? = null,
    val pagesRead: Int = 0
)

internal fun FileDto.toBusiness() = FileBusiness(
    title = title,
    author = author,
    path = path,
    genre = genre,
    annotation = annotation,
    fileExtension = fileExtension,
    size = size,
    pages = pages,
    language = language,
    parentPath = parentPath,
    pagesRead = pagesRead
)