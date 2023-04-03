package com.swalif.sa.core.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.core.graphics.decodeBitmap
import androidx.core.net.toUri
import androidx.core.os.BuildCompat
import com.swalif.sa.BuildConfig
import com.swalif.sa.datasource.local.dao.MessageDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import logcat.logcat
import java.io.File
import java.io.IOException
import java.util.*
import javax.inject.Inject

class FilesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val contentResolver = context.contentResolver

    // bf9c06d3-10b9-47b3-89bd-fb318d186687.jpeg
    suspend fun saveImage(uri: Uri, nameFile: String): Boolean {
        return try {
            withContext(Dispatchers.Default) {
                val imageDecoder =
                    ImageDecoder.createSource(contentResolver, uri).decodeBitmap { info, source ->
                    }
                context.openFileOutput(nameFile, Context.MODE_PRIVATE)
                    .use {
                        val formatImage: CompressFormat
                        val quality: Int
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            formatImage = CompressFormat.WEBP_LOSSY
                            quality = 0
                        } else {
                            formatImage = CompressFormat.JPEG
                            quality = 80
                        }
                        if (!imageDecoder.compress(formatImage, quality, it)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                true
            }

        } catch (e: Exception) {
            logcat { e.message!! }
            false
        }

    }
}

data class MediaStorage(
    val nameFile: String,
    val contentUri: Uri,
//    val typeMedia:MediaType
)

enum class MediaType(ext: String) {
    IMAGE("png"), VIDEO("---"), AUDIO("---")
}