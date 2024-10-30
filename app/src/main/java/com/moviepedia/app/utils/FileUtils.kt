package com.moviepedia.app.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class FileUtils {

    companion object{

         fun copyUriToAppStorage(context: Context, uri: Uri): String? {
            val contentResolver = context.contentResolver
            val fileName = getFileName(context,uri) ?: return null
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val copiedFile = File(storageDir, fileName)

            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(copiedFile).use { outputStream ->
                    copyStream(inputStream, outputStream)
                }
            }

            return copiedFile.absolutePath
        }

        @SuppressLint("Range")
        private fun getFileName(context: Context, uri: Uri): String? {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        result = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                    }
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result?.lastIndexOf('/')
                if (cut != -1) {
                    result = result?.substring(cut!! + 1)
                }
            }
            return result
        }

        private fun copyStream(input: InputStream, output: OutputStream) {
            val buffer = ByteArray(1024)
            var length: Int
            while (input.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }
        }
    }


}