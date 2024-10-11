package com.semonemo.presentation.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

fun saveBitmapToGallery(
    context: Context,
    bitmap: Bitmap,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100,
    frameTitle: String,
    nickname: String,
    folderName: String = "세모내모",
    onSuccess: (Uri) -> Unit,
) {
    val fileName = "${nickname}의 $frameTitle"
    val fos: OutputStream?
    val filenameWithExtension = "$fileName.${getExtensionForFormat(format)}"
    var imageUri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues =
            ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filenameWithExtension)
                put(MediaStore.MediaColumns.MIME_TYPE, getMimeTypeForFormat(format))
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/$folderName",
                )
            }

        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    } else {
        val imagesDir =
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                folderName,
            )
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        val imageFile = File(imagesDir, filenameWithExtension)
        fos = FileOutputStream(imageFile)
    }

    fos?.use {
        bitmap.compress(format, quality, it)
        imageUri?.let { uri ->
            onSuccess(uri)
        }
    }
    MediaScannerConnection.scanFile(
        context,
        arrayOf(
            File(
                Environment.DIRECTORY_PICTURES,
                "$folderName/$filenameWithExtension",
            ).toString(),
        ),
        null,
        null,
    )
}

private fun getMimeTypeForFormat(format: Bitmap.CompressFormat): String =
    when (format) {
        Bitmap.CompressFormat.JPEG -> "image/jpeg"
        Bitmap.CompressFormat.PNG -> "image/png"
        else -> "image/png"
    }

private fun getExtensionForFormat(format: Bitmap.CompressFormat): String =
    when (format) {
        Bitmap.CompressFormat.JPEG -> "jpg"
        Bitmap.CompressFormat.PNG -> "png"
        else -> "png"
    }
