package com.example.videoapp.utils

import android.content.Context
import android.net.Uri
import java.io.File

fun convertUriToFile(context: Context, uri: Uri, suffix: String): File {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val tempFile = File.createTempFile("upload", suffix, context.cacheDir)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }

    return tempFile
}