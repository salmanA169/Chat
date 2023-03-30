package com.swalif.sa.core.storage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.storage.StorageManager
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import logcat.logcat
import java.util.UUID
import javax.inject.Inject

class FilesManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val storageManager = context.getSystemService(StorageManager::class.java)
    private val contentResolver = context.contentResolver
    private val _mediaStorage = MutableStateFlow<List<MediaStorage>>(emptyList())
    val mediaStorageFlow = _mediaStorage.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private fun updateMedia() {
        coroutineScope.cancel()
        coroutineScope.launch {
            val files = context.filesDir.listFiles()
            val mapFiles = files?.map {
                MediaStorage(it.name, it.toUri())
            }
            _mediaStorage.update {
                mapFiles ?: listOf()
            }
        }
    }

    // TODO: improve saveFile with return boolean
    fun saveFile(btm: Bitmap) {
        context.openFileOutput(UUID.randomUUID().toString().plus(".png"), Context.MODE_PRIVATE)
            .use {
                btm.compress(Bitmap.CompressFormat.PNG,100,it)
                updateMedia()
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