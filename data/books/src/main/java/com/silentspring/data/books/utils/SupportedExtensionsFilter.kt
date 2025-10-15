package com.silentspring.data.books.utils

import com.silentspring.data.books.data.model.FileExtension
import java.io.File
import java.io.FileFilter

internal object SupportedExtensionsFilter : FileFilter {
    override fun accept(file: File): Boolean {

        val lowerCase = file.name.lowercase()

        FileExtension.getAllSupportedExtensions().forEach {
            if (lowerCase.endsWith(it)) return true
        }

        return false
    }
}