package com.swalif.sa.core.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.core.graphics.decodeBitmap
import androidx.core.net.toUri
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
    suspend fun saveImage(uri: Uri,nameFile:String): Boolean {
        return try {
            withContext(Dispatchers.Default) {
                val imageDecoder =
                    ImageDecoder.createSource(contentResolver, uri).decodeBitmap { info, source ->
                    }
                context.openFileOutput(nameFile, Context.MODE_PRIVATE)
                    .use {
                         if(!imageDecoder.compress(Bitmap.CompressFormat.JPEG, 80, it)){
                             throw IOException("Couldn't save bitmap")
                         }
                    }
                true
            }

        }catch (e:Exception){
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