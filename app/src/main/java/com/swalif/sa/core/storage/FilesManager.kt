package com.swalif.sa.core.storage

import android.content.Context
import android.os.storage.StorageManager
import logcat.logcat

class FilesManager(context:Context) {
    private val storageManager = context.getSystemService(StorageManager::class.java)

    init {
        val uuidFile = storageManager.getUuidForPath(context.filesDir)
        val  availableBytes = storageManager.getAllocatableBytes(uuidFile)

        logcat {
            (availableBytes).toString()
        }
        storageManager.allocateBytes(uuidFile,1024*1024*10)
    }
}