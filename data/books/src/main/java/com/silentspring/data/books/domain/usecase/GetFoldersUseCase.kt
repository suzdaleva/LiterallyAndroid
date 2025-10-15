package com.silentspring.data.books.domain.usecase

import com.silentspring.data.books.domain.BooksRepository
import com.silentspring.data.books.domain.model.FolderBusiness
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetFoldersUseCase @Inject constructor(
    val repository: BooksRepository
) {
    suspend operator fun invoke(): List<FolderBusiness> {
       return withContext(Dispatchers.IO) {
            repository.getFolders()
        }
    }
}