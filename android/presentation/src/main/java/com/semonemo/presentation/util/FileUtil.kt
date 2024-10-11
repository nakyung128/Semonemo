package com.semonemo.presentation.util

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun converterFile(bitmap: Bitmap): File {
    val tempFile = File.createTempFile("upload_", ".png")
    FileOutputStream(tempFile).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    return tempFile
}
