package com.silentspring.data.books.utils

import java.io.File
import java.io.FileFilter

internal object SupportedFilesFilter : FileFilter {
    override fun accept(file: File): Boolean {
        return file.isDirectory
    }
}