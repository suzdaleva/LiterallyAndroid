package com.silentspring.data.books.domain

import com.silentspring.data.books.domain.model.FolderBusiness

interface BooksRepository {
    fun getFolders(): List<FolderBusiness>
}