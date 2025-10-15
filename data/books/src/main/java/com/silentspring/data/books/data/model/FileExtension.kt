package com.silentspring.data.books.data.model

enum class FileExtension(val extensions: List<String>, mimeTypes: List<String>) {
    PDF(
        extensions = listOf("pdf", "xps"),
        mimeTypes = listOf("application/pdf", "application/oxps", "application/vnd.ms-xpsdocument")
    ),
    EPUB(extensions = listOf("epub"), mimeTypes = listOf("application/epub+zip")),
    TIFF(extensions = listOf("tiff", "tif"), mimeTypes = listOf("image/tiff")),
    FB2(
        extensions = listOf("fb2", "fbd"),
        mimeTypes = listOf(
            "application/fb2",
            "application/x-fictionbook",
            "application/x-fictionbook+xml",
            "application/x-fb2",
            "application/fb2+zip",
            "application/fb2.zip",
            "application/x-zip-compressed-fb2"
        )
    ),
    DJVU(
        extensions = listOf("djvu"),
        mimeTypes = listOf("image/vnd.djvu", "image/djvu", "image/x-djvu")
    );

    companion object {
        fun getAllSupportedExtensions(): List<String> {
            return entries.map { it.extensions }.flatten()
        }
    }
}