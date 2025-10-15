package com.silentspring.data.books.data

import android.os.Environment
import com.silentspring.data.books.domain.BooksRepository
import com.silentspring.data.books.domain.model.FolderBusiness
import com.silentspring.data.books.utils.SupportedFilesFilter
import java.io.File
import javax.inject.Inject

internal class BooksRepositoryImpl @Inject constructor() : BooksRepository {
    override fun getFolders(): List<FolderBusiness> {
        val result = mutableListOf<FolderBusiness>()
        val file = File(Environment.getExternalStorageDirectory().path)

        if (!file.isDirectory) {
            return result
        }

        val listFiles = file.listFiles(SupportedFilesFilter)

        listFiles?.forEach { file ->
            file.listFiles()?.size

            val fileDto = FolderBusiness(
                title = file.name,
                filesCount = file.listFiles()?.size ?: 0
            )

            result.add(fileDto)
        }

        return result
    }
}